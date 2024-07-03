<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.PodaciRadara"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Početna stranica</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
</head>
<body>
	<h1>Radari - Početna stranica</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna
				radari</a></li>

	</ul>
	<div class="form-container">
		<div>
			<h2>Obriši sve radare</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/obrisiSveRadare">
				<table>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Obriši sve radare"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Obriši radar</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/radari/obrisiRadar">
				<table>
					<tr>
						<td>Radar id</td>
						<td><input name="idRadar" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Obriši radar"></td>
					</tr>
				</table>
			</form>
		</div>


	</div>
			<%
		String odgovor = (String) request.getAttribute("odgovor");
		if(odgovor != null){
		%>
		<p>
			odgovor :
			<%=odgovor%>
		</p>
		<%
		}
		%>
</body>
</html>