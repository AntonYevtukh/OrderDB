<%--
  Created by IntelliJ IDEA.
  User: Anton
  Date: 14.10.2017
  Time: 11:39
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="partials/meta.jsp"%>
</head>
<body>
    <%@ include file="partials/header.jsp"%>
    <div class="main_area">
        <div class="content">
            <h2>Order #${order.id}</h2>
            <c:if test="${success_message != null}">
                <span class="success"><c:out value="${success_message}"/></span>
            </c:if>

            <c:if test="${error_message != null}">
                <span class="error"><c:out value="${error_message}"/></span>
            </c:if>
            <br>
            <label class="info_label">Client:</label>
            <label class="info_label">${order.client.getFullName()}</label><br>
            <label class="info_label">Date/Time:</label>
            <label class="info_label"><fmt:formatDate value="${order.timestamp}"
                                                      pattern="MM.dd.yyyy '/' HH:mm:ss" /></label><br><br>
            <label class="info_label">Items:</label>

            <div class="table_container">
                <table border="1">
                    <tr>
                        <th width="20%">Product Id</th>
                        <th width="40%">Product Name</th>
                        <th width="20%">Count</th>
                        <th width="20%">Product Price</th>
                    </tr>
                    <c:forEach items="${order.items}" var="item">
                        <tr>
                            <td align="right"><c:out value="${item.product.id}"/></td>
                            <td><c:out value="${item.product.name}"/></td>
                            <td align="right"><c:out value="${item.count}"/></td>
                            <td align="right"><fmt:formatNumber
                                    type = "number" pattern="#,###,###.00" value = "${item.product.price}" var="pattern"/>
                                        ${fn:replace(pattern, ",", " ")}
                            </td>
                        </tr>
                    </c:forEach>
                </table>
                <a class="left" href="/orders?action=get_all"><button type="button" class="button">Back</button></a>
                <label class="info_label right right_text top_margin right_margin"><fmt:formatNumber
                        type = "number" pattern="#,###,###.00" value = "${order.totalPrice}" var="pattern"/>
                    ${fn:replace(pattern, ",", " ")}</label>
                <label class="info_label right right_text top_margin">Total:</label><br>
            </div>
        </div>
    </div>
    <%@ include file="partials/footer.jsp"%>
</body>
</html>
