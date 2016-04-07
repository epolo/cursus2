<%@page import="java.net.URL"%>
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
String extSess = null;
for (Cookie c : request.getCookies()) {
	if ("EXTSESSION".equals(c.getName())) {
		extSess = c.getValue();
		break;
	}
}
StringBuffer u = request.getRequestURL();
int i = u.indexOf("/", 8);
if (i > 0) {
	u = u.delete(i, u.length());
}
u.append("/cursus2/userid");
if (extSess != null)
	u.append("?ext=").append(extSess);
%>
<p>
	extSess: <%= extSess %>
	<br/>
	url: <%= u %>
</p>
<p>
<%
	URL url = new URL(u.toString());
	byte[] buf = new byte[256];
	int sz = url.openStream().read(buf);
	String userId = sz > 0? new String(buf, 0, sz): " -- nothing --";
%>
user ID: <%= userId %>
</p>
<p>
	<a href='/cursus2'> Back to the Club</a>
</p>
    </body>
</html>
