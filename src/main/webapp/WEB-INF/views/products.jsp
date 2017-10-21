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
            <h2>Products</h2>
            <form class="form" action="/products?action=add" method="POST">
                <div class="add">
                    <label class="input_label">Name*</label>
                    <input ype="text" name="name" value="${name}">
                    <label class="input_label">Price*</label>
                    <input class="right" type="number" step="0.01" name="price" value="${price}"><br>
                    <label class="input_label">Description</label>
                    <div class="textarea_container">
                        <textarea rows="5" name="description">${description}</textarea><br>
                    </div>
                    <input class="button" type="submit" value="Add"><br>
                </div>
            </form>
            <c:if test="${success_message != null}">
                <span class="success"><c:out value="${success_message}"/></span>
            </c:if>

            <c:if test="${error_message != null}">
                <span class="error"><c:out value="${error_message}"/></span>
            </c:if>
            <c:if test="${products != null}">
                <div class="table_container">
                    <table border="1">
                        <tr>
                            <th width="10%">Id</th>
                            <th width="25%">Name</th>
                            <th width="50%">Description</th>
                            <th width="15%">Price</th>
                        </tr>
                        <c:forEach items="${products}" var="product">
                            <tr>
                                <td align="right"><c:out value="${product.id}"/></td>
                                <td><c:out value="${product.name}"/></td>
                                <td><c:out value="${product.description}"/></td>
                                <td align="right"><fmt:formatNumber type = "number"
                                     pattern="#,###,###.00" value = "${product.price}" var="pattern"/>
                                        ${fn:replace(pattern, ",", " ")}
                                </td>
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
