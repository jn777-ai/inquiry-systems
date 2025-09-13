<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %> 
<c:url var="inquiryUrl" value="/inquiry" /> 
<c:url var="historyUrl" value="/inquiry?action=history" /> 
<!DOCTYPE html> 
<html lang="ja"> 
<head> 
<meta charset="UTF-8"> 
<title>お問い合わせフォーム</title> 
<link rel="stylesheet" href="style.css"> 
</head> 
<body> 
<div class="container"> 
    <h1>お問い合わせフォーム</h1> 
   
<form action="${inquiryUrl}" method="post" onsubmit="return validateForm()">
    <input type="hidden" name="action" value="submit" />

    <p>
        <label for="name">ニックネーム:</label>
        <input type="text" id="name" name="name" value="<c:out value='${inquiry.name}'/>">
        <span class="error-message"><c:out value='${errors.name}' /></span>
    </p>

    <p>
        <label for="employeeId">社員番号:</label>
        <input type="text" id="employeeId" name="employeeId" value="<c:out value='${inquiry.employeeId}'/>">
        <span class="error-message"><c:out value='${errors.employeeId}' /></span>
    </p>

    <p>
        <label for="genre">ジャンル:</label>
        <select id="genre" name="genre">
            <option value="">選択してください</option>
            <option value="フロア" <c:if test="${inquiry.genre == 'フロア'}">selected</c:if>>フロア</option>
            <option value="コンセッション" <c:if test="${inquiry.genre == 'コンセッション'}">selected</c:if>>コンセッション</option>
            <option value="ゴミ捨て" <c:if test="${inquiry.genre == 'ゴミ捨て'}">selected</c:if>>ゴミ捨て</option>
            <option value="設備" <c:if test="${inquiry.genre == '設備(休憩室や更衣室など）'}">selected</c:if>>設備(休憩室や更衣室など)</option>
 			<option value="その他" <c:if test="${inquiry.genre == 'その他'}">selected</c:if>>その他</option>
        </select>
        <span class="error-message"><c:out value='${errors.genre}' /></span>
    </p>

    <p>
        <label for="content">内容:</label>
        <textarea id="content" name="content" rows="5" cols="40" maxlength="120"><c:out value='${inquiry.content}'/></textarea>
        <span class="error-message"><c:out value='${errors.content}' /></span>
    </p>

    <p>
        <label for="captcha">スパム対策: <c:out value='${captchaQuestion}' /></label>
        <input type="text" id="captcha" name="captcha" required>
        <span class="error-message"><c:out value='${errors.captcha}' /></span>
    </p>

 <div class="buttons-wrapper">
    <div class="confirm-button">
        <input type="submit" value="確認">
    </div>
    <div class="history-button">
        <a href="${historyUrl}" class="button secondary">お問い合わせ履歴を見る</a>
    </div>
</div>
 
</div> 
 
<script>
function validateForm() {
    let isValid = true;

    const name = document.getElementById('name'); 
    const employeeId = document.getElementById('employeeId');
    const genre = document.getElementById('genre');
    const content = document.getElementById('content');
    const captcha = document.getElementById('captcha');

    
    document.querySelectorAll('.error-message').forEach(el => el.textContent = '');

    
    if (employeeId.value.trim() === '') {
        employeeId.nextElementSibling.textContent = '社員番号は必須です。';
        isValid = false;
    } else if (!/^\d{6}$/.test(employeeId.value)) {
        employeeId.nextElementSibling.textContent = '社員番号は6桁の数字で入力してください。';
        isValid = false;
    }

    
    if (genre.value === '') {
        genre.nextElementSibling.textContent = 'ジャンルを選択してください。';
        isValid = false;
    }

   
    if (content.value.trim() === '') {
        content.nextElementSibling.textContent = '内容は必須です。';
        isValid = false;
    }

 
    if (captcha.value.trim() === '') {
        captcha.nextElementSibling.textContent = 'スパム対策の質問に答えてください。';
        isValid = false;
    }

    return isValid;
}

</script>

 
</body> 
</html>