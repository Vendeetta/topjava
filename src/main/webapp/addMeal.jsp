<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@taglib uri="http://topjava.ru/functions" prefix="f" %>

<html>
<head>
    <title>Add meal</title>
</head>
<body>
<ul style="font-size: large">
    <li><a href="index.html">Home</a></li>

</ul>
<hr>
<h2><p>Add meal</p></h2>

<form method="POST" action='meals' name="update">
    <table>
        <tr>
            <td>
                DateTime:
            </td>
            <td>
                <input type="datetime-local" size="20" name="dateTime"
                       value="${f: formatLocalDateTime(date, "yyyy-MM-dd HH:mm")}"/> <br/>

            </td>
        </tr>
        <td>
            Description:
        </td>
        <td>
            <input type="text" size="20" name="description"/>
        </td>
        </tr>
        <tr>
            <td>
                Calories:
            </td>
            <td>
                <input type="number" size="20" name="calories"/>
                <br/>
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
