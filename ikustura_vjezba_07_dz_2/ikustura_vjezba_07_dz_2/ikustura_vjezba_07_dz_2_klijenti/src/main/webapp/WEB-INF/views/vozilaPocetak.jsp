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
	<h1>Praćene vožnje - Početna stranica</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/vozila/pocetak">Početna
				vozila</a></li>

		<li></li>
	</ul>
	<div class="form-container">
		<div>
			<h2>Praćene vožnje u vremenskom intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVoznji">
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
			<h2>Praćene vožnje vozila</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVoznjiVozila">
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
			<h2>Praćene vožnje vozila u vremenskom intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pretrazivanjeVoznjiInterval">
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
		
	</div>
	<div class="form-container">
	<div>
			<h2>Pokreni vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/pokreniVozilo">
				<table>
					<tr>
						<td>Vozilo id:</td>
						<td><input name="idVozilo" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Pokreni vozilo"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Zaustavi vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/vozila/zaustaviVozilo">
				<table>
					<tr>
						<td>Vozilo id:</td>
						<td><input name="IdVozilo" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Zaustavi vozilo"></td>
					</tr>
				</table>
			</form>
		</div>
	</div>


</body>
</html>
