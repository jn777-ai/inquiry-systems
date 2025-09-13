<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%> 
<!DOCTYPE html> 
<html lang="ja"> 
<head> 
    <meta charset="UTF-8"> 
    <title>お問い合わせ完了</title> 
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
    
</head> 
<body> 
    <div class="container"> 
        <h1>お問い合わせ完了</h1> 
        <p class="success-message">お問い合わせいただき、誠にありがとうございます。内容を確認後、改めてご連絡させていた
だきます。</p> 
        <div class="button-group"> 
            <a href="${pageContext.request.contextPath}/inquiry" class="button secondary">トップに戻る</a> 
            
        </div> 
    </div> 
</body> 
</html>