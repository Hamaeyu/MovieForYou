<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css" />
</head>
<body>
	<jsp:include page="/WEB-INF/views/header.jsp" />
	<div class="page-wrapper">
    <div class="signup-container">
        <h2>회원가입</h2>
        <form action="${pageContext.request.contextPath}/signup.user" method="post">
            <input type="hidden" name="action" value="register">

            <div class="form-group">
                <label for="nickname">닉네임</label>
                <input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력해 주세요" required>
            </div>

            <div class="form-group">
                <label for="email">이메일</label>
                <input type="email" id="email" name="email" placeholder="이메일을 입력해 주세요" required>
            </div>

            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" placeholder="영문자, 숫자, 특수문자 포함 8~20자" required>
                <p class="info-text">영문자, 숫자, 특수문자를 포함하여 8~20자로 입력하세요.</p>
            </div>

            <div class="form-group">
                <label for="confirmPw">비밀번호 확인</label>
                <input type="password" id="confirmPw" name="confirmPw" placeholder="비밀번호를 다시 입력해 주세요" required>
            </div>

            <button type="submit" class="signup-btn">회원가입</button>

           <div class="login-link">
			    이미 계정이 있으신가요? <a href="${pageContext.request.contextPath}/login.auth"> 로그인하기</a>
			</div>
        </form>
    </div>
   </div> 
     <jsp:include page="/WEB-INF/views/footer.jsp" />
</body>
</html>
