package web;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import web.mbeans.AppMBean;

public class UserIdServlet extends HttpServlet {

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String ext = request.getParameter("ext");
		if (ext == null || ext.trim().isEmpty())
			return;
		
		AppMBean app = (AppMBean) getServletContext().getAttribute("app");
		if (app == null)
			return;
		
		Integer id = app.getExtUsersMap().get(ext);
		if (id != null) {
			response.setContentType("text/plain");
			response.getWriter().print(id.toString());
		}
	}
}
