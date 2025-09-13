<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html lang="ja"> 
<head> 
    <meta charset="UTF-8"> 
    <title>お問い合わせ内容確認</title> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    
</head> 
<body> 
    <div class="container"> 
        <h1>お問い合わせ内容確認</h1> 
        <p>以下の内容でよろしいですか？</p> 

        <div class="display-info"> 
            <p><strong>ニックネーム:</strong> <%= request.getAttribute("name") %></p> 
            <p><strong>社員番号:</strong> <%= request.getAttribute("employeeId") %></p> 
            <p><strong>ジャンル:</strong> <%= request.getAttribute("genre") %></p> 
            <div class="content-block">
  			<strong>内容:</strong>
  			<pre class="wrapped-pre"><%= request.getAttribute("content") %></pre>
			</div>
            
            
        </div> 

        <form action="${pageContext.request.contextPath}/inquiry" method="post">
            <input type="hidden" name="action" value="complete">
            <div class="button-group">
                <input type="submit" value="送信">
                <a href="${pageContext.request.contextPath}/inquiry" class="button secondary">戻る</a>
            </div>
        </form>
    </div> 
</body> 
</html>
