<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@taglib uri="http://topjava.ru/functions" prefix="f" %>

<html>
<head>
    <title>${meal.id == null ? 'Add meal' : 'Edit meal'}</title>
</head>
<body>
<ul style="font-size: large">
    <li><a href="index.html">Home</a></li>

</ul>
<hr>
<h2><p>${meal.id == null ? 'Add meal' : 'Edit meal'}</p></h2>

<form method="POST" action='meals' name="update">
    <table>
        <input type="text" hidden="hidden" name="mealId"
               value="${meal.id}"/>
        <tr>
            <td>
                DateTime:
            </td>
            <td>
                <input type="datetime-local" size="20" name="dateTime"
                       value ="${f : formatLocalDateTime(meal.dateTime)}"/>
            </td>
        </tr>
        <td>
            Description:
        </td>
        <td>
            <input type="text" size="20" name="description"
                   value="${meal.description}"/> <br/>
        </td>
        </tr>
        <tr>
            <td>
                Calories:
            </td>
            <td>
                <input type="number" size="20" name="calories"
                       value="${meal.calories}"/> <br/>
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
