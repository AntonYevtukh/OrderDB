<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 14.10.2017
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="partials/meta.jsp"%>
</head>
<body>
    <%@ include file="partials/header.jsp"%>
    <div class="main_area">
        <div class="content">
            <h2>Clients</h2>
            <form class="form" action="/clients?action=add" method="POST">
                <div class="add">
                    <label class="input_label">First Name*</label>
                    <input type="text" name="first_name" value="${first_name}">
                    <label class="input_label">Last Name*</label>
                    <input class="right" type="text" name="last_name" value="${last_name}">
                    <label class="input_label">Age</label>
                    <input type="number" name="age" value="${age}"><br>
                    <input class="button" type="submit" value="Add"><br>
                </div>
            </form>
            <c:if test="${success_message != null}">
                <span class="success"><c:out value="${success_message}"/></span>
            </c:if>

            <c:if test="${error_message != null}">
                <span class="error"><c:out value="${error_message}"/></span>
            </c:if>
            <c:if test="${clients != null}">
                <div class="table_container">
                    <table border="1">
                        <tr>
                            <th width="10%">Id</th>
                            <th width="40%">First Name</th>
                            <th width="40%">Last Name</th>
                            <th width="10%">Age</th>
                        </tr>
                        <c:forEach items="${clients}" var="client">
                            <tr>
                                <td align="right"><c:out value="${client.id}"/></td>
                                <td><c:out value="${client.firstName}"/></td>
                                <td><c:out value="${client.lastName}"/></td>
                                <td align="right"><c:out value="${client.age == null ? '--' : client.age}"/></td>
                            </tr>
                        </c:forEach>
                    </table>
                </div>
            </c:if>
            <a class="left" href="/"><button type="button" class="button">Back</button></a>
        </div>
    </div>
    <%@ include file="partials/footer.jsp"%>
</body>
</html>
