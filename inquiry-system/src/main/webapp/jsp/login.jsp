<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>管理者ログイン</title>
   
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
    <div class="container">
        <h1>管理者ログイン</h1>
        <form action="${pageContext.request.contextPath}/auth" method="post">
            <p>
                <label for="username">ユーザー名:</label>
                <input type="text" id="username" name="username">
            </p>
            <p>
                <label for="password">パスワード:</label>
                <input type="password" id="password" name="password">
            </p>
            <div class="button-group">
                <input type="submit" value="ログイン" class="button">
            </div>
            <c:if test="${not empty loginError}">
                <p class="error-message">${loginError}</p>
            </c:if>
        </form>
    </div>
</body>
</html>

