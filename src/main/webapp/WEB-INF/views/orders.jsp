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
            <h2>Orders</h2>
            <form class="form" action="/orders?action=add" method="POST">
                <div class="add">
                    <label class="input_label">Client*</label>
                    <select name="client_id">
                        <option selected disabled value="-1">--Not selected--</option>
                        <c:forEach items="${clients}" var="client">
                            <option value="${client.id}" ${client.id == client_id ? 'selected' : ''}>
                                <c:out value="${client.getFullName()}"/></option>
                        </c:forEach>
                    </select><br>
                    <c:forEach begin="0" end="${max_items_count}" var="i">
                        <label class="input_label">Product</label>
                        <select name="<c:out value="product_id_${i}"/>">
                            <option selected value="-1">--Not selected--</option>
                            <c:forEach items="${products}" var="product">
                                <option value="${product.id}" ${product.id == product_ids.get(i) ? 'selected' : ''}>
                                    <c:out value="${product.name}"/></option>
                            </c:forEach>
                        </select>
                        <label class="input_label">Count</label>
                        <input type="number" min="1" name="<c:out value="count_${i}"/>"
                               value="${counts == null ? 1 : counts.get(i)}">
                    </c:forEach>

                    <input class="button" type="submit" value="Create"><br>
                </div>
            </form>
            <c:if test="${success_message != null}">
                <span class="success"><c:out value="${success_message}"/></span>
            </c:if>

            <c:if test="${error_message != null}">
                <span class="error"><c:out value="${error_message}"/></span>
            </c:if>
            <c:if test="${orders != null && !orders.isEmpty()}">
                <div class="table_container">
                    <table border="1">
                        <tr>
                            <th width="10%">Id</th>
                            <th width="35%">Client</th>
                            <th width="25%">Date/Time</th>
                            <th width="20%">Total Price</th>
                            <th width="10%">Details</th>
                        </tr>
                        <c:forEach items="${orders}" var="order">
                            <tr>
                                <td align="right"><c:out value="${order.id}"/></td>
                                <td><c:out value="${order.client.getFullName()}"/></td>
                                <td align="center"><fmt:formatDate value="${order.timestamp}"
                                                    pattern="MM.dd.yyyy '/' HH:mm:ss" /></td>
                                <td align="right"><fmt:formatNumber type = "number"
                                     pattern="#,###,###.00" value = "${order.totalPrice}" var="pattern"/>
                                        ${fn:replace(pattern, ",", " ")}
                                </td>
                                <td align="center"><a class="link" href="/orders?action=get&id=${order.id}">Details</a></td>
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
