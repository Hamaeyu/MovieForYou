<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>회원가입</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        window.APP_CTX = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/signup.js"></script>
</head>
<body>
<div class="page-wrapper">
    <div class="signup-container" id="signup-container">
        <h2>회원가입</h2>
        <form id="signupForm">
            
            <!-- 이메일 -->
            <div class="form-group">
                <label for="email">이메일</label>
                <div class="input-row">
                    <input type="email" id="email" name="email" placeholder="이메일을 입력하세요" required>
                    <button type="button" id="checkEmailBtn" class="check-btn">중복확인</button>
                </div>
                <div id="emailMsg" class="info-text"></div>
            </div>

            <!-- 이메일 인증 -->
            <div class="form-group">
                <button type="button" id="sendCodeBtn" class="check-btn disabled" disabled>이메일 인증</button>
                <div id="verifyArea" style="display:none; margin-top:10px;">
                    <input type="text" id="verifyCode" placeholder="인증번호 입력" />
                    <button type="button" id="checkCodeBtn" class="check-btn">확인</button>
                    <div id="verifyMsg" class="info-text"></div>
                </div>
            </div>

            <!-- 비밀번호 -->
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" placeholder="비밀번호를 입력하세요" required disabled>
                <div id="passwordMsg" class="info-text">영문, 숫자, 특수문자 포함 5~20자</div>
            </div>

            <!-- 비밀번호 확인 -->
            <div class="form-group">
                <label for="confirmPw">비밀번호 확인</label>
                <input type="password" id="confirmPw" placeholder="비밀번호 확인" required disabled>
                <div id="confirmMsg" class="info-text"></div>
            </div>

            <!-- 닉네임 -->
            <div class="form-group">
                <label for="nickname">닉네임</label>
                <div class="input-row">
                    <input type="text" id="nickname" name="nickname" placeholder="닉네임을 입력하세요" required disabled>
                    <button type="button" id="checkNickBtn" class="check-btn">중복확인</button>
                </div>
                <div id="nickMsg" class="info-text"></div>
            </div>

            <!-- 회원가입 버튼 -->
            <button type="button" id="signupBtn" class="signup-btn" disabled>회원가입</button>
        </form>

        <div class="login-link">
            이미 계정이 있으신가요? <a href="${pageContext.request.contextPath}/login.auth">로그인</a>
        </div>
    </div>
</div>
</body>
</html>
