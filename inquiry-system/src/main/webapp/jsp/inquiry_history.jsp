<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ja">
<head>
    <meta charset="UTF-8">
    <title>お問い合わせ履歴</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css">
</head>
<body>
<div class="container">
    <h1>お問い合わせ履歴</h1>

    <form method="get" action="${pageContext.request.contextPath}/inquiry">
        <input type="hidden" name="action" value="history" />

        <p>
            <label for="genre">ジャンルで絞り込む:</label>
            <select name="genre" id="genre">
                <option value="">全て表示</option>
                <option value="フロア" <c:if test="${selectedGenre == 'フロア'}">selected</c:if>>フロア</option>
                <option value="コンセッション" <c:if test="${selectedGenre == 'コンセッション'}">selected</c:if>>コンセッション</option>
                <option value="ゴミ捨て" <c:if test="${selectedGenre == 'ゴミ捨て'}">selected</c:if>>ゴミ捨て</option>
                <option value="設備" <c:if test="${selectedGenre == '設備'}">selected</c:if>>設備</option>
                <option value="その他" <c:if test="${selectedGenre == 'その他'}">selected</c:if>>その他</option>
            </select>
        </p>

        <p>
            <label for="status">ステータスで絞り込む:</label>
            <select name="status" id="status">
                <option value="">全て表示</option>
                <option value="新規" <c:if test="${selectedStatus == '新規'}">selected</c:if>>新規</option>
                <option value="対応中" <c:if test="${selectedStatus == '対応中'}">selected</c:if>>対応中</option>
                <option value="対応済み" <c:if test="${selectedStatus == '対応済み'}">selected</c:if>>対応済み</option>
            </select>
        </p>

        <div class="button-group">
            <input type="submit" value="絞り込む" class="button secondary" />
        </div>
    </form>

    <hr>

   
    <c:choose>
        <c:when test="${not empty inquiries}">
            <c:forEach var="inquiry" items="${inquiries}" varStatus="status">
                <div class="inquiry-item">
                    <h3>お問い合わせ #${status.count}</h3>

                    <div class="display-info">
                        <p><strong>ニックネーム:</strong> <c:out value="${inquiry.name}" /></p>
                        <p><strong>社員番号:</strong> <c:out value="${inquiry.employeeId}" /></p>
                        <p><strong>ジャンル:</strong> <c:out value="${inquiry.genre}" /></p>
                        <p><strong>ステータス:</strong> <c:out value="${inquiry.status}" /></p>
                    </div>

                    <p><strong>内容:</strong></p>
                    <pre class="wrapped-pre"><c:out value="${inquiry.content}" /></pre>

                  
<div class="button-row">

    <form action="${pageContext.request.contextPath}/inquiry" method="post">
        <input type="hidden" name="action" value="updateStatus" />
        <input type="hidden" name="id" value="${inquiry.id}" />

        <label for="status-${inquiry.id}" class="status-label">ステータス:</label>
        <select id="status-${inquiry.id}" name="newStatus">
            <option value="新規" <c:if test="${inquiry.status == '新規'}">selected</c:if>>新規</option>
            <option value="対応中" <c:if test="${inquiry.status == '対応中'}">selected</c:if>>対応中</option>
            <option value="対応済み" <c:if test="${inquiry.status == '対応済み'}">selected</c:if>>対応済み</option>
        </select>

        <input type="submit" value="更新" class="button secondary" />
    </form>

    <form action="${pageContext.request.contextPath}/inquiry" method="post"
          onsubmit="return confirm('本当に削除しますか？');">
        <input type="hidden" name="action" value="delete" />
        <input type="hidden" name="id" value="${inquiry.id}" />
        <input type="submit" value="削除" class="button danger" />
    </form>

</div>


                </div>
                <hr>
            </c:forEach>
        </c:when>
        <c:otherwise>
            <p>まだお問い合わせはありません。</p>
        </c:otherwise>
    </c:choose>


    <div class="button-group">
        <a href="${pageContext.request.contextPath}/inquiry" class="button secondary">お問い合わせフォームに戻る</a>
    </div>
</div>
</body>
</html>


