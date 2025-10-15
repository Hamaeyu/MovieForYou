<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>window.APP_CTX = '${pageContext.request.contextPath}';</script>
    <script src="${pageContext.request.contextPath}/js/signup.js"></script>
</head>
<body>
<div class="page-wrapper">
    <div class="signup-container">
        <h2>회원가입</h2>
        <form id="signupForm" method="post" action="${pageContext.request.contextPath}/signup.user">

            <!-- 이메일 -->
            <div class="form-group">
                <label for="email">이메일</label>
                <div class="input-row">
                    <input type="email" id="email" name="email" placeholder="이메일을 입력하세요" required>
                    <button type="button" id="checkEmailBtn" class="check-btn">중복확인</button>
                </div>
                <div id="emailMsg" class="info-text"></div>
            </div>

            <!-- 비밀번호 -->
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" placeholder="비밀번호 입력" disabled>
                <div id="pwMsg" class="info-text">영문자, 숫자, 특수문자를 포함하여 5~20자를 입력하세요.</div>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="form-group">
                <label for="confirmPw">비밀번호 확인</label>
                <input type="password" id="confirmPw" name="confirmPw" placeholder="비밀번호 확인" disabled>
                <div id="confirmMsg" class="info-text"></div>
            </div>

            <!-- 닉네임 -->
            <div class="form-group">
                <label for="nickname">닉네임</label>
                <div class="input-row">
                    <input type="text" id="nickname" name="nickname" placeholder="닉네임 입력" disabled>
                    <button type="button" id="checkNickBtn" class="check-btn" disabled>중복확인</button>
                </div>
                <div id="nickMsg" class="info-text"></div>
            </div>

            <!-- 이메일 인증 버튼 -->
            <button type="button" id="verifyEmailBtn" class="signup-btn disabled" disabled>이메일 인증</button>

            <!-- 회원가입 버튼 -->
            <button type="submit" id="registerBtn" class="signup-btn" disabled>회원가입</button>
        </form>
    </div>
</div>
</body>
</html>
