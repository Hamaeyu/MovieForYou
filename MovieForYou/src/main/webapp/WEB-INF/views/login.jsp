<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body class="login-page">
    <div class="login-wrapper">
    <div class="login-container">
        <div class="header">
            <div class="icon"><i class="fa-duotone fa-solid fa-user"></i></div>
            <div class="header-text">
                <h1>로그인</h1>
                <p>Sign in to your account</p>
            </div>
        </div>

        <form method="post" action="/loginok.auth" >
            <div class="form-group">
                <label for="email">Email</label>
                <input type="text" id="email" name="email" placeholder="이메일을 입력하세요." required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요." required>
            </div>
            <div class="form-group">
                <c:if test="${not empty requestScope.errorMsg}">
                    <div class="error">${requestScope.errorMsg}</div>
                    <!-- 한 번만 보여주고 제거 -->
                    <c:remove var="errorMsg" scope="request"/>
                </c:if>
            </div>
            <div class="remember-forgot">
                <a href="/forgotInfo">아이디/비밀번호 찾기</a>
            </div>

            <div class="button-group">
                <button id="login" type="submit" class="btn-login">로그인</button>
            </div>
        </form>

        <div class="signup-link">
            계정이 없으신가요? <a href="/signup">회원가입</a>
        </div>
        </div>
    </div>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/login.js"></script>
</html>