<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, java.util.Date, java.text.SimpleDateFormat, edu.unizg.foi.nwtis.ikustura.vjezba_07_dz_2.podaci.Voznja"%>
<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>REST MVC - Pregled kazni</title>
<link rel="stylesheet" type="text/css"
	href="${pageContext.servletContext.contextPath}/resources/css/nwtis.css">

</head>
<body>
	<h1>REST MVC - Pregled vožnji</h1>
	<ul>
	<li><a
			href="${pageContext.servletContext.contextPath}">Početna
				stranica</a></li>
		<li><a
			href="${pageContext.servletContext.contextPath}/mvc/simulacije/pocetak">Početna
				stranica simulacije</a></li>
	</ul>
	<br />
	<table>
		<tr>
			<th>R.br.</th>
			<th>Vozilo</th>
			<th>Vrijeme</th>
			<th>Broj</th>
			<th>Brzina</th>
			<th>snaga</th>
			<th>struja</th>
			<th>visina</th>
			<th>gpsBrzina</th>
			<th>Temperatura vozila</th>
			<th>Postotak baterija</th>
			<th>Napon baterija</th>
			<th>Kapacitet baterija</th>
			<th>Temperatura baterija</th>
			<th>Preostalo km</th>
			<th>Ukupno km</th>
			<th>GDP širina</th>
			<th>GPS dužina</th>
		</tr>
		<%
		int i = 0;
		SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
		List<Voznja> voznje = (List<Voznja>) request.getAttribute("voznje");
			for (Voznja voznja : voznje) {
			  i++;
			  Date vrijeme = new Date(voznja.getVrijeme() * 1000);
			%>
			<tr>
				<td class="desno"><%=i%></td>
				<td><%=voznja.getId()%></td>
				<td><%=voznja.getBroj()%></td>
				<td><%=sdf.format(vrijeme)%></td>
				<td><%=voznja.getBrzina()%></td>
				<td><%=voznja.getSnaga()%></td>
				<td><%=voznja.getStruja()%></td>
				<td><%=voznja.getVisina()%></td>
				<td><%=voznja.getGpsBrzina()%></td>
				<td><%=voznja.getTempVozila()%></td>
				<td><%=voznja.getPostotakBaterija()%></td>
				<td><%=voznja.getNaponBaterija()%></td>
				<td><%=voznja.getKapacitetBaterija()%></td>
				<td><%=voznja.getTempBaterija()%></td>
				<td><%=voznja.getPreostaloKm()%></td>
				<td><%=voznja.getUkupnoKm()%></td>
				<td><%=voznja.getGpsSirina()%></td>
				<td><%=voznja.getGpsDuzina()%></td>
			</tr>
			<%
			}
	
			%>
		

	</table>
</body>
</html>
