<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head><title>エラー</title></head>
<body>
<h2>エラーが発生しました</h2>
<p>${errorMessage}</p>
<a href="<%=request.getContextPath()%>/index.jsp">戻る</a>
</body>
</html>
