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
	<h1>Radari - ispis</h1>
	<ul>
        <li><a href="${pageContext.servletContext.contextPath}">Početna stranica</a></li>
        <li><a href="${pageContext.servletContext.contextPath}/mvc/radari/pocetak">Početna radari</a></li>
    </ul>
    <%
        int i = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss.SSS");
        List<PodaciRadara> radari = (List<PodaciRadara>) request.getAttribute("radari");
        if (radari == null || radari.isEmpty()) {
    %>
        <p>Nema radara u bazi.</p>
    <%
        } else {
    %>
    <table>
        <tr>
            <th>R.br.</th>
            <th>Radar</th>
            <th>Adresa</th>
            <th>Mrežna vrata</th>
            <th>GPS širina</th>
            <th>GPS dužina</th>
        </tr>
        <%
            for (PodaciRadara r : radari) {
                i++;
        %>
        <tr>
            <td class="desno"><%= i %></td>
            <td><%= r.id() %></td>
            <td><%= r.adresaRadara() %></td>
            <td><%= r.mreznaVrataRadara() %></td>
            <td><%= r.gpsSirina() %></td>
            <td><%= r.gpsDuzina() %></td>
        </tr>
        <%
            }
        %>
    </table>
    <%
        }
    %>
</body>
</html>
	
	
	
	
	
	