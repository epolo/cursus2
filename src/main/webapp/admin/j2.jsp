<%@page import="java.util.List"%>
<%@page import="javax.persistence.*"%>
<%@page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>JSP Page</title>
    </head>
    <body>
        <h1>Hello World!</h1>
<%
request.setCharacterEncoding("UTF-8");
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

Query: <%= query %>

<%
	if (!query.isEmpty()) {
		EntityManager em = Persistence.createEntityManagerFactory("cursus2PU").createEntityManager();
		Query q = em.createQuery(query);
		if (query.toLowerCase().startsWith("select")) {
			List res = q.getResultList();
%>
Result size = <%= res.size() %>

<%
		for (Object o : res) {
%>
	<%= o %>
<%
			}
		} else {
			em.getTransaction().begin();
			int n = q.executeUpdate();
			em.getTransaction().commit();
%>
Updated <%= n %> records
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
