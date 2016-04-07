package web;

import db.entity.Users;
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
import web.mbeans.ContextMBean;

public class GoogleAuthServlet extends HttpServlet {

	static final String OAUTH_ENDPOINT = "https://accounts.google.com/o/oauth2/";
	static final String PROP_CLI_ID = "oauth2.cli.id";
	static final String PROP_CLI_SECR = "oauth2.cli.secret";
	static final String PROP_REDIR_URL = "oauth2.redir.url";

	private final Logger logger = Logger.getLogger(GoogleAuthServlet.class.getName());
	private String loginPath;
	private String callbackPath;
	private String accessDeniedRedir;
	private String welcomeRedir;

	@Override
	public void init() {
		loginPath = getInitParameter("login-path");
		if (loginPath == null)
			loginPath = "/login";
		callbackPath = getInitParameter("callback-path");
		if (callbackPath == null)
			callbackPath = "/callback";
		accessDeniedRedir = getInitParameter("access-denied-redir");
		welcomeRedir = getInitParameter("welcome-redir");
		if (welcomeRedir == null)
			welcomeRedir = "/welcome.jsf";
	}

	private Properties getProps() {
		return ((web.mbeans.AppMBean)getServletContext().getAttribute("app")).getGgleProps();
	}
	
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		String sp = request.getServletPath();
		
		// login stage 1 -- send redir to Google
		if (loginPath.equals(sp)) {
			String redir = request.getParameter("redir");
			if (redir == null)
				redir = welcomeRedir;
			String url = getRedirUrl(redir);
			response.sendRedirect(url);
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
		
		String state = request.getParameter("state");
		String code = request.getParameter("code");
		if (code == null) {
			logger.severe("Bad response from Google OAuth2 server -- no 'code' parameter.");
			response.sendError(HttpServletResponse.SC_BAD_GATEWAY, 
							"Bad response from Google OAuth2 server -- no 'code' parameter.");
			return;
		}

		HttpURLConnection conn = (HttpURLConnection) new URL(OAUTH_ENDPOINT + "token").openConnection();
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

		Users u = new Users();
		u.setEmail(email);
		u.setGgleUid(id);
		u.setName(name);
		ContextMBean ctx = (ContextMBean) request.getSession().getAttribute("ctx");
		ctx.updateExtCookie(request, response);

		s = request.getContextPath();
		boolean b = ctx.authorize(u);
		if (b) {
			int i = state.indexOf("cursus_redir:");
			if (i >= 0) {
				s += state.substring(i + "cursus_redir:".length());
			}
		} else {
			s += "/invitation.jsf";
		}
		response.sendRedirect(s);

/*		
			response.setContentType("text/plain");
			response.getWriter().println(" -- Response parsed: name = " + name + ", email = " + email 
					+"\n All parsed data: " + p
					+"\n state: " + state
			);
*/
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

	private String getRedirUrl(String cursus_redir) {
		Properties p = getProps();
		return OAUTH_ENDPOINT 
				+ "auth?approval_prompt=force&access_type=online&response_type=code&scope=profile+email"
				+ "&redirect_uri=" + p.getProperty(PROP_REDIR_URL)
				+ "&client_id=" + p.getProperty(PROP_CLI_ID)
				+ "&state=cursus_redir:" + cursus_redir;
	}
	
	private String getPostData(String code) {
		Properties p = getProps();
		return "code=" + code
			+ "&client_id=" + p.getProperty(PROP_CLI_ID)
			+ "&client_secret=" + p.getProperty(PROP_CLI_SECR)
			+ "&redirect_uri=" + p.getProperty(PROP_REDIR_URL)
			+ "&grant_type=authorization_code";
	}

}
