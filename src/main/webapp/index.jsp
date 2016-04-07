<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Test SMF forum</title>
    </head>
    <body>
        <h1>Hello SMF!</h1>
		
<%
StringBuffer u = request.getRequestURL();
int i = u.indexOf("/", 8);
if (i > 0) {
	u = u.delete(i, u.length());
}
u.append("/cursus2/userid");
%>
<p>
	u: <%= u %>
</p>
    </body>
</html>
