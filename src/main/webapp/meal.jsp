<%--
  Created by IntelliJ IDEA.
  User: spaikk
  Date: 05.02.2023
  Time: 15:04
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://topjava.ru/functions" prefix="f" %>

<html>
<head>
    <title>Edit meal</title>
</head>
<body>
<ul style="font-size: large">
    <li><a href="index.html">Home</a></li>

</ul>
<hr>
<h2><p>Edit meal</p></h2>

<form method="POST" action='meals' name="update">
    <table>
        <tr>
            <td>
                Meal ID:
            </td>
            <td><input type="text" readonly="readonly" size="20" name="mealId"
                       value="<c:out value="${meal.id}"/>"/> <br/>
            </td>
        </tr>
        <tr>
            <td>
                DateTime:
            </td>
            <td>
                <input type="datetime-local" size="20" name="dateTime"
                       value="${meal.dateTime}"/> <br/>
            </td>
        </tr>
        <td>
            Description:
        </td>
        <td>
            <input type="text" size="20" name="description"
                   value="<c:out value="${meal.description}" />"/> <br/>
        </td>
        </tr>
        <tr>
            <td>
                Calories:
            </td>
            <td>
                <input type="number" size="20" name="calories"
                       value="<c:out value="${meal.calories}" />"/> <br/>
        <tr>
            <td>
                <input type="submit" value="Submit"/>
            </td>
            <td>
                <button onclick="window.history.back()" type="button">Cancel</button>
            </td>
        </tr>
    </table>
</form>
</body>
</html>
