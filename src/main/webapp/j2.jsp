<%@page import="java.util.List"%>
<%@page import="javax.persistence.EntityManager"%>
<%@page import="javax.persistence.Persistence"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
<%
String query = request.getParameter("query");
if (query == null)
	query = "";
else
	query = query.trim();
%>

<form action="j2.jsp" method="POST">
	<p>
	<textarea cols="70" rows="20" name="query"><%= query %></textarea>
	<br/>
	<input type="submit" value="Query"/>
	<a href="j2.jsp?reload=yes"> Clear cache </a>
	</p>
</form>
		
<pre>
<%
	if (!query.isEmpty()) {
		EntityManager em = Persistence.createEntityManagerFactory("cursus2PU").createEntityManager();
		List res = em.createQuery(query).getResultList();
%>
Result size = <%= res.size() %>
<%
		for (Object o : res) {
%>
	<%= o %>
<%
		}
	}
%>
</pre>

<%
	if (request.getParameter("reload") != null) {
		Persistence.createEntityManagerFactory("cursus2PU").getCache().evictAll();
%>
<p> The entities cache is cleared. </p>
<%
	}
%>

    </body>
</html>
