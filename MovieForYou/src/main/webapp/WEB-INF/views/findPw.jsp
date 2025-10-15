<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>비밀번호 찾기</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
    <style>
        .findpw-container {
            background: #2a2a2a;
            border-radius: 12px;
            padding: 40px;
            width: 100%;
            max-width: 420px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            margin: 100px auto;
        }
        .success-msg { color: limegreen; font-size: 13px; }
        .error-msg { color: red; font-size: 13px; }
    </style>
</head>
<body>
    <div class="findpw-container">
        <h2>비밀번호 찾기</h2>
        <div class="form-group">
            <label>이메일</label>
            <input type="email" id="email" placeholder="가입한 이메일을 입력하세요">
        </div>
        <div class="form-group">
            <label>닉네임</label>
            <input type="text" id="nickname" placeholder="가입한 닉네임을 입력하세요">
        </div>
        <div id="findMsg" class="info-text"></div>

        <button type="button" id="findBtn" class="signup-btn">인증번호 전송</button>

        <div id="resetSection" style="display:none; margin-top:30px;">
            <label>새 비밀번호</label>
            <input type="password" id="newPw" placeholder="새 비밀번호 입력">
            <label>비밀번호 확인</label>
            <input type="password" id="newPwConfirm" placeholder="비밀번호 확인">
            <div id="pwMsg" class="info-text"></div>
            <button type="button" id="resetBtn" class="signup-btn" disabled>비밀번호 재설정</button>
        </div>
    </div>

    <script>
        let emailVerified = false;

        // ✅ 이메일 + 닉네임 확인
        $("#findBtn").click(function() {
            const email = $("#email").val();
            const nickname = $("#nickname").val();

            $.post("${pageContext.request.contextPath}/findPw.user", { email, nickname }, function(res) {
                if (res.trim() === "FOUND") {
                    $("#findMsg").text("인증메일을 발송할 수 있습니다.").css("color", "limegreen");
                    window.open("${pageContext.request.contextPath}/email.user?email=" + email,
                        "emailVerify", "width=480,height=400");
                } else {
                    $("#findMsg").text("입력하신 정보로 가입된 계정이 없습니다.").css("color", "red");
                }
            });
        });

        // ✅ 부모창에서 호출되는 콜백
        window.emailVerified = function() {
            emailVerified = true;
            $("#findMsg").text("이메일 인증 완료 ✅").css("color", "limegreen");
            $("#resetSection").slideDown();
        };

        // ✅ 비밀번호 유효성 체크
        $("#newPwConfirm").on("blur", function() {
            const pw = $("#newPw").val();
            const confirm = $("#newPwConfirm").val();
            const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{5,20}$/;
            if (!pwRegex.test(pw)) {
                $("#pwMsg").text("비밀번호 조건을 만족하지 않습니다.").css("color", "red");
                $("#resetBtn").prop("disabled", true);
            } else if (pw !== confirm) {
                $("#pwMsg").text("비밀번호가 일치하지 않습니다.").css("color", "red");
                $("#resetBtn").prop("disabled", true);
            } else {
                $("#pwMsg").text("가능한 비밀번호입니다.").css("color", "limegreen");
                $("#resetBtn").prop("disabled", false);
            }
        });

        // ✅ 비밀번호 재설정
        $("#resetBtn").click(function() {
            const email = $("#email").val();
            const password = $("#newPw").val();
            $.post("${pageContext.request.contextPath}/resetPw.user", { email, password }, function() {
                alert("비밀번호가 재설정되었습니다.");
                window.location.href = "${pageContext.request.contextPath}/login.user";
            });
        });
    </script>
</body>
</html>
