<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>게시글 상세보기 화면</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/resources/css/view.css">
</head>
<body>
	<div class="container">
		<h2>${board.title}</h2>
		<p>${board.content}</p>
		<small><fmt:formatDate value="${board.createdAt}" pattern="yyyy년MM월dd일 HH:mm"/></small>
	</div>
	<div class="update">
	<c:if test="${board.user == principal.id}">
		<a href="${pageContext.request.contextPath}/board/update?boardId=${board.id}" class="btn btn-edit">수정</a>
		<a href="${pageContext.request.contextPath}/board/delete?boardId=${board.id}" class="btn btn-delete">삭제</a>
	</c:if>
	<a href="${pageContext.request.contextPath}/board/list?page=1" class="list">목록으로 돌아가기</a>
	</div>
	
	<c:forEach var="comment" items="${commentList}">
		<div class="comment">
			<div class="comment-author">${comment.username}</div>		
			<div class="comment-date"><small><fmt:formatDate value="${comment.createdAt}" pattern="yyyy년MM월dd일 HH:mm"/></small></div>
			<c:choose>
			<c:when test="">
				<form action="${pageContext.request.contextPath}/board/updateComment" method="post">
    		    	<input type="hidden" name="boardId" value="${board.id}" />
      		  		<textarea name="content" rows="4" required>원래 댓글</textarea>
	     		   	<button type="submit" class="btn btn-comment">수정하기</button>
  			  	</form>
			</c:when>
			<c:otherwise>
				<div class="comment-content">${comment.content}</div>
			</c:otherwise>
			</c:choose>
			<c:if test="${comment.userId == principal.id}">
				<a class="btn btn-edit" href="${pageContext.request.contextPath}/board/updateComment?commentId=${comment.id}">수정</a>
				<a class="btn btn-delete" href="${pageContext.request.contextPath}/board/deleteComment?boardId=${comment.boardId}&commentId=${comment.id}">삭제</a>
			</c:if>
		</div>
	</c:forEach>
<div class="comment-form">
	<h3>댓글</h3>
    <form action="${pageContext.request.contextPath}/board/addComment" method="post">
        <input type="hidden" name="boardId" value="${board.id}" />
        <textarea name="content" rows="4" required></textarea>
        <br>
        <button type="submit" class="btn btn-comment">댓글 작성</button>
    </form>
</div>
</body>
</html>