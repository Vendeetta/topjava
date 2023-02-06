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
        ${meal.excess ?  '<tr style="color: red">' : '<tr style="color: green">'}
        <td>${f: formatLocalDateTime(meal.dateTime, "yyyy-MM-dd HH:mm")}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
        <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
        </tr>
    </c:forEach>
</table>

</body>
</html>
