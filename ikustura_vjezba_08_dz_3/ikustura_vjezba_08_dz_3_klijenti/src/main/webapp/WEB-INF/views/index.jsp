<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Početna stranica</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">
</head>
<body>
	<h1>Kazne - Početna stranica</h1>
	<ul>
		<li><a href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/pocetak">Početna
				kazne</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazni">Ispis
				svih kazni</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/kazne/ispisKazniVozila">Kazne
				vozila</a></li>

		<li></li>
	</ul>

	<div class="form-container">
		<div>
			<h2>Pretraživanje kazni u intervalu</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazni">
				<table>
					<tr>
						<td>Od vremena:</td>
						<td><input name="odVremena" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>Do vremena:</td>
						<td><input name="doVremena" />
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kazne "></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Pretraživanje kazni s rednim brojem</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/pretrazivanjeKazniRb">
				<table>
					<tr>
						<td>Redni broj</td>
						<td><input name="redniBroj" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value=" Dohvati kazne redni broj"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Provjeri poslužitelja</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/posluzitelj">
				<table>
					<tr>
						<td>Provjeri</td>
						<td><input type="submit" value="Provjeri poslužitelja"></td>
					</tr>
				</table>
			</form>
		</div>
	</div>
</body>
</html>
