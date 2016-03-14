<%@page import="java.sql.ResultSet"%>
<%@page import="java.sql.Connection"%>
<%@page import="javax.sql.DataSource"%>
<%@page import="javax.naming.InitialContext"%>
<%@page import="javax.naming.Context"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
<pre>
<%
// Context initContext = new InitialContext();
// Context envContext  = (Context)initContext.lookup("java:/comp/env");
DataSource ds = (DataSource)
		new InitialContext().lookup("java:/comp/env/jdbc/cursus");
		// envContext.lookup("jdbc/cursus");
Connection conn = ds.getConnection();
ResultSet rs = null;
try {
	rs = conn.createStatement().executeQuery("SELECT id, name FROM disciplines");
	while (rs.next()) {
%>
	<%= rs.getInt(1) %>: <%= rs.getString(2) %>
<%		
	}
} finally {
	if (rs != null) try { rs.close(); } catch(Exception ignore) {}
	if (conn != null) try { conn.close(); } catch(Exception ignore) {}
}
%>
</pre>
    </body>
</html>
