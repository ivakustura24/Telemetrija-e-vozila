<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Početna stranica</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
</head>
<body>
	<h1>Vožnje - Početna stranica</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Početna
				vozila</a></li>

		<li></li>
		</li>
	</ul>
	<div class="form-container">
		<div>
			<h2>Vožnje u vremenskom intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznji">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Vrati voznje"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Vožnje vozila</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznjiVozila">
				<table>
					<tr>
						<td>Vozilo id:</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Vrati voznje vozila"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Vožnje vozila u vremenskom intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pretrazivanjeVoznjiInterval">
				<table>
					<tr>
						<td>Vozilo id:</td>
						<td><input name="idVozila" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Vrati voznje"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Pokreni simulaciju</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/simulacije/pokreniSimulaciju">
				<table>
					<tr>
						<td>Csv datoteka:</td>
						<td><input name="csvDatoteka" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Trajanje pauze:</td>
						<td><input name="trajanjePauze" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Trajanje sekundi:</td>
						<td><input name="trajanjeSekundi" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Pokreni simulaciju"></td>
					</tr>
				</table>
			</form>
		</div>

	</div>



</body>
</html>
