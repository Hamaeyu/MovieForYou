<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
	
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/email.css">
    <title>이메일 인증</title>
    <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
</head>
<body>
    <h2>이메일 인증</h2>
    <p>입력하신 이메일 주소로 인증번호를 전송합니다.</p>
    <input type="text" id="verifyCode" placeholder="인증번호 입력">
    <button id="sendCodeBtn" class="btn-send">인증번호 전송</button>
    <button id="checkCodeBtn">확인</button>
    <div id="msg"></div>

    <script>
        const email = new URLSearchParams(window.location.search).get("email");
        let sent = false;

        $("#sendCodeBtn").click(function(){
            $.post("${pageContext.request.contextPath}/sendVerifyCode.user", { email }, function(res){
                if(res.trim()==="SENT"){
                    $("#msg").text("인증번호가 이메일로 전송되었습니다.").css("color","limegreen");
                    sent = true;
                } else {
                    $("#msg").text("이메일 전송 실패").css("color","red");
                }
            });
        });

        $("#checkCodeBtn").click(function(){
            const code = $("#verifyCode").val();
            if(!sent) return alert("먼저 인증번호를 전송하세요.");
            $.post("${pageContext.request.contextPath}/verifyCode.user", { code }, function(res){
                if(res.trim()==="OK"){
                    $("#msg").text("인증 완료되었습니다!").css("color","limegreen");
                    opener.emailVerified();
                    setTimeout(()=>window.close(),1000);
                } else {
                    $("#msg").text("인증번호가 올바르지 않습니다.").css("color","red");
                }
            });
        });
    </script>
</body>
</html>
