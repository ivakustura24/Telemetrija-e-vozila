<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled kazni</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">

</head>
<body>
	<h1>REST MVC - Aktivnost poslužitelja</h1>
	<ul>
	<li><a
			href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
				 kazne</a></li>
	</ul>
	<%
        String odgovor = (String) request.getAttribute("odgovor");
        
    %>
	<p>
		<%=odgovor%>
	</p>
		

	
</body>
</html>
