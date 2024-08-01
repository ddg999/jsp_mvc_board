<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>JSP MVC 게시판</title>
<style type="text/css">
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: 'Roboto', Arial, sans-serif;
	background-color: #f9f9f9;
	color: #333;
	margin: 0;
	padding: 0;
	display: flex;
	justify-content: center;
	align-items: center;
	height: 100vh;
}

.container {
	text-align: center;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
	padding: 40px;
	width: 400px;
	max-width: 90%;
}

h2 {
	color: #333;
	font-size: 28px;
	margin-bottom: 20px;
}

.nav-list {
	list-style: none;
	padding: 0;
	margin-top: 20px;
}

.nav-list li {
	margin: 10px;
	display: block;
}

.nav-list li a:hover {
	background-color: #0056b3;
}

.nav-list li a {
	display: block;
	width:30vh;
	text-decoration: none;
	padding: 12px 24px;
	color: #fff;
	background-color: #007bff;
	border-radius: 25px;
	transition: background-color 0.3s ease;
	display: inline-block;
	font-size: 16px;
}

.btn-primary {
	background-color: #007bff;
	color: #fff;
	border: none;
	padding: 12px 24px;
	border-radius: 25px;
	cursor: pointer;
	font-size: 16px;
	transition: background-color 0.3s ease;
}

.btn-secondary {
	background-color: #007bff;
	color: #fff;
	border: none;
	padding: 12px 24px;
	border-radius: 25px;
	cursor: pointer;
	font-size: 16px;
	transition: background-color 0.3s ease;
}
</style>
</head>
<body>
	<div class="container">
		<h2>JSP MVC 게시판 테스트 페이지</h2>
		<ul class="nav-list">
			<li><a href="/t-board/user/signup">회원가입</a></li>
			<li><a href="/t-board/user/signin">로그인</a></li>
			<li><a href="/t-board/user/logout">로그아웃</a></li>
			<li><a href="/t-board/board/list">게시판목록</a></li>
		</ul>
	</div>
</body>
</html>