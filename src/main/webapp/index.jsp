<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<html>
<head>
    <%@ include file="/WEB-INF/views/partials/meta.jsp"%>
</head>
<body>
    <%@ include file="/WEB-INF/views/partials/header.jsp"%>
    <div class="main_area">
        <div class="buttons_container">
            <h1>Select page</h1>
            <div class="links">
                <a href="/clients?action=get">
                    <button type="button" class="button_large">Clients</button>
                </a>
                <a href="/products?action=get" class="button_large">
                    <button type="button" class="button_large">Products</button>
                </a>
                <a href="/orders?action=get_all" class="button_large">
                    <button type="button" class="button_large">Orders</button>
                </a>
            </div>
        </div>
    </div>
    <%@ include file="/WEB-INF/views/partials/footer.jsp"%>
</body>

