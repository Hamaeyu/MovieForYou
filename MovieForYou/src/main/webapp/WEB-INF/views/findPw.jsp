<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <script>
        window.APP_CTX = '${pageContext.request.contextPath}';
    </script>
    <script src="${pageContext.request.contextPath}/js/findPw.js"></script>
</head>
<body>
<div class="page-wrapper">
    <div class="signup-container" id="findPw-container">
        <h2>비밀번호 재설정</h2>

        <!-- 이메일 + 닉네임 -->
        <div class="form-group">
            <label for="email">이메일</label>
            <input type="email" id="email" placeholder="가입 시 사용한 이메일을 입력하세요" required>
        </div>

        <div class="form-group">
            <label for="nickname">닉네임</label>
            <input type="text" id="nickname" placeholder="닉네임을 입력하세요" required>
            <div id="checkMsg" class="info-text"></div>
        </div>

        <!-- 이메일 인증 -->
        <div class="form-group">
            <button type="button" id="sendCodeBtn" class="check-btn" disabled>이메일 인증</button>
            <div id="verifyArea" style="display:none; margin-top:10px;">
                <input type="text" id="verifyCode" placeholder="인증번호 입력" />
                <button type="button" id="checkCodeBtn" class="check-btn">확인</button>
                <div id="verifyMsg" class="info-text"></div>
            </div>
        </div>

        <!-- 새 비밀번호 -->
        <div class="form-group">
            <label for="newPw">새 비밀번호</label>
            <input type="password" id="newPw" placeholder="새 비밀번호 입력" required disabled>
            <div id="pwMsg" class="info-text"></div>
        </div>

        <!-- 비밀번호 확인 -->
        <div class="form-group">
            <label for="confirmPw">비밀번호 확인</label>
            <input type="password" id="confirmPw" placeholder="비밀번호 확인" required disabled>
            <div id="confirmMsg" class="info-text"></div>
        </div>

        <!-- 재설정 버튼 -->
        <button type="button" id="resetBtn" class="signup-btn" disabled>비밀번호 재설정</button>
    </div>
</div>
</body>
</html>
