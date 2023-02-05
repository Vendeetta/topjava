<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://topjava.ru/functions" prefix="f" %>
<html>
<head>
    <title>HELLO, MEALS!</title>
</head>
<body>
<ul style="font-size: large">
    <li><a href="index.html">Home</a></li>

</ul>
<hr>
<h3>Meals</h3>
<p><a href="meals?action=add">Add meal</a></p>
<table border="1">

    <tr>
        <td>Date</td>
        <td>Description</td>
        <td>Calories</td>
        <td></td>
        <td></td>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <c:if test="${meal.excess==false}">
            <tr style="color: green">
        </c:if>
        <c:if test="${meal.excess==true}">
            <tr style="color: red">
        </c:if>
        <td>${f: formatLocalDateTime(meal.dateTime, "dd.MM.yyyy HH.mm.ss")}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=update&id=<c:out value="${meal.id}"/>">Update</a></td>
        <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
