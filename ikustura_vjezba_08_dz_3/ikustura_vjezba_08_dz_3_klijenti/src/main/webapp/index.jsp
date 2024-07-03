<%@ page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Početna stranica</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
</head>
<body>
	<h1>Početna stranica</h1>
	<ul>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Kazne</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Radari</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Vozila</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Simulacije</a></li>
	</ul>
</body>
</html>
