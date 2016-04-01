package web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Properties;
import java.util.logging.Logger;
import javax.json.Json;
import javax.json.stream.JsonParser;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class GoogleAuthServlet extends HttpServlet {

	private final Logger logger = Logger.getLogger(GoogleAuthServlet.class.getName());
	private String cliId;
	private String cliSecret;
	private String redirUrl;
	private String loginPath;
	private String callbackPath;
	private String accessDeniedRedir;

	@Override
	public void init() {
		loginPath = getInitParameter("login-path");
		if (loginPath == null)
			loginPath = "/login";
		callbackPath = getInitParameter("callback-path");
		if (callbackPath == null)
			callbackPath = "/callback";
		accessDeniedRedir = getInitParameter("access-denied-redir");

		Properties props = ((web.mbeans.AppMBean)getServletContext().getAttribute("app")).getGgleProps();
		cliId = props.getProperty("oauth2.cli.id");
		cliSecret = props.getProperty("oauth2.cli.secret");
		redirUrl = props.getProperty("oauth2.redir.url");
	}

	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String sp = request.getServletPath();
		
		// login stage 1 -- send redir to Google
		if (loginPath.equals(sp)) {
			response.sendRedirect(getRedirUrl());
			return;
		}
		
		if (!callbackPath.equals(sp)) {
			throw new ServletException("Unrecognized servlet path: " + sp);
		}

		// login stage 2 -- get redir from Google
		if (request.getParameter("error") != null) {
			logger.warning("User denies access to Google+ account");
			if (accessDeniedRedir == null)
				response.sendError(HttpServletResponse.SC_FORBIDDEN, "You deny access to your Google+ data");
			else
				response.sendRedirect(accessDeniedRedir);
			return;
		}
		
		String code = request.getParameter("code");
		if (code == null) {
			logger.severe("Bad response from Google OAuth2 server -- no 'code' parameter.");
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, 
							"Bad response from Google OAuth2 server -- no 'code' parameter.");
			return;
		}

		HttpURLConnection conn = (HttpURLConnection) new URL("https://accounts.google.com/o/oauth2/token")
											.openConnection();
		conn.setDoOutput(true);
		conn.setRequestMethod("POST");
		OutputStream os = conn.getOutputStream();
		String s = getPostData(code);
		os.write(s.getBytes());
		os.flush();

		int ok = conn.getResponseCode();
		if (ok != HttpURLConnection.HTTP_OK) {
			String msg = "Bad response from Google OAuth2-get-access-token request: " + ok 
						+ " -- " + conn.getResponseMessage();
			logger.severe(msg);
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, msg);
			return;
		}

		Properties p = processJson(conn.getInputStream());
		String t = p.getProperty("access_token");
		if (t == null) {
			String msg = "Bad response from Google OAuth2-get-access-token request -- no 'access_token' parameter";
			logger.severe(msg);
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, msg);
			return;
		}
		
		p = processJson(new URL("https://www.googleapis.com/oauth2/v2/userinfo?access_token=" + t).openStream());
		String id = p.getProperty("id");
		String name = p.getProperty("name");
		String firstName = p.getProperty("given_name");
		String lastName = p.getProperty("family_name");
		String email = p.getProperty("email");

		
			response.setContentType("text/plain");
			response.getWriter().println(" -- Response parsed: name = " + name + ", email = " + email 
					+"\n All parsed data: " + p
			);

	}


	/**
	 * Parsing only trivial plain structures
	 */
	private Properties processJson(InputStream is) {
		Properties prop = new Properties();
		JsonParser par = Json.createParser(is);
		String key = null;
		while (par.hasNext()) {
			switch (par.next()) {
				case KEY_NAME: 
					key = par.getString(); 
					break;
				case VALUE_STRING: 
					if (key != null)
						prop.setProperty(key, par.getString()); 
					key = null; 
					break;
			}
		}
		return prop;
	}

	private String getRedirUrl() {
		return "https://accounts.google.com/o/oauth2/auth?client_id=" + cliId
				+ "&response_type=code&scope=profile+email&redirect_uri=" + redirUrl
				+ "&state=retrieve-google-profile&access_type=online";
	}
	
	private String getPostData(String code) {
		return "code=" + code
			+ "&client_id=" + cliId
			+ "&client_secret=" + cliSecret
			+ "&redirect_uri=" + redirUrl
			+ "&grant_type=authorization_code";
	}

}
