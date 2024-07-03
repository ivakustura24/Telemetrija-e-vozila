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
	<h1>Kazne - Ispis kazni vozila</h1>
	<ul>
	<li><a
			href="${pageContext.servletContext.contextPath}">Početna
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

		</li>
	</ul>
	<div class="form-container">
		<div>
			<h2>Pretraživanje kazni u intervalu za odabrano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/KazneVoziloInterval">
				<table>
					<tr>
						<td>Vozilo id</td>
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
						<td><input type="submit"
							value="Dohvati kazne vozilo interval"></td>
					</tr>
				</table>
			</form>
		</div>
		<div>
			<h2>Pretraživanje kazni za odabrano vozilo</h2>
			<form method="post"
				action="${pageContext.servletContext.contextPath}/mvc/kazne/KazneVozilo">
				<table>
					<tr>
						<td>Vozilo id</td>
						<td><input name="id" /> <input type="hidden"
							name="${mvc.csrf.name}" value="${mvc.csrf.token}" /></td>
					</tr>
					<tr>
						<td>&nbsp;</td>
						<td><input type="submit" value="Dohvati kazne za vozilo"></td>
					</tr>
				</table>
			</form>
		</div>

	</div>

</body>
</html>
