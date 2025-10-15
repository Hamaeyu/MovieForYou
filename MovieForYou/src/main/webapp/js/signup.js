$(document).ready(function() {
    let emailValid = false, pwValid = false, confirmValid = false, nickValid = false;

    // ✅ 이메일 중복확인
    $("#checkEmailBtn").click(function() {
        const email = $("#email").val();
        if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)) {
            $("#emailMsg").text("이메일 형식이 올바르지 않습니다.").css("color", "red");
            return;
        }
        $.get("${pageContext.request.contextPath}/checkEmail.user", { email }, function(res) {
            if (res.trim() === "EXISTS") {
                $("#emailMsg").text("중복된 이메일입니다.").css("color", "red");
                emailValid = false;
                disableBelow("#password");
            } else {
                $("#emailMsg").text("가능한 이메일입니다.").css("color", "limegreen");
                emailValid = true;
                $("#password").prop("disabled", false).focus();
            }
        });
    });

    // ✅ 비밀번호 유효성 검사
    $("#password").on("blur", function() {
        const pw = $(this).val();
        const pwRegex = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[!@#$%^&*]).{5,20}$/;
        if (!pwRegex.test(pw)) {
            $("#pwMsg").text("비밀번호 조건을 만족하지 않습니다.").css("color", "red");
            pwValid = false;
            disableBelow("#confirmPw");
        } else {
            $("#pwMsg").text("가능한 비밀번호입니다.").css("color", "limegreen");
            pwValid = true;
            $("#confirmPw").prop("disabled", false).focus();
        }
    });

    // ✅ 비밀번호 일치 검사
    $("#confirmPw").on("blur", function() {
        const pw = $("#password").val();
        const confirm = $(this).val();
        if (pw !== confirm) {
            $("#confirmMsg").text("비밀번호가 일치하지 않습니다.").css("color", "red");
            confirmValid = false;
            disableBelow("#nickname");
        } else {
            $("#confirmMsg").text("비밀번호가 일치합니다.").css("color", "limegreen");
            confirmValid = true;
            $("#nickname, #checkNickBtn").prop("disabled", false);
        }
    });

    // ✅ 닉네임 중복확인
    $("#checkNickBtn").click(function() {
        const nickname = $("#nickname").val();
        if (nickname.length < 2) {
            $("#nickMsg").text("닉네임은 2자 이상이어야 합니다.").css("color", "red");
            return;
        }
        $.get("${pageContext.request.contextPath}/checkNickname.user", { nickname }, function(res) {
            if (res.trim() === "EXISTS") {
                $("#nickMsg").text("중복된 닉네임입니다.").css("color", "red");
                nickValid = false;
            } else {
                $("#nickMsg").text("가능한 닉네임입니다.").css("color", "limegreen");
                nickValid = true;
                $("#verifyEmailBtn").prop("disabled", false).removeClass("disabled");
            }
        });
    });

    // ✅ 이메일 인증 버튼 클릭
    $("#verifyEmailBtn").click(function() {
        if (!(emailValid && pwValid && confirmValid && nickValid)) return;
        window.open("${pageContext.request.contextPath}/email.jsp?email=" + $("#email").val(),
            "emailVerify", "width=480,height=400");
    });

    // ✅ 비활성화 함수
    function disableBelow(selector) {
        const all = ["#password", "#confirmPw", "#nickname", "#checkNickBtn", "#verifyEmailBtn"];
        const idx = all.indexOf(selector);
        for (let i = idx + 1; i < all.length; i++) $(all[i]).prop("disabled", true);
    }

    // ✅ 이메일 인증 완료 콜백
    window.emailVerified = function() {
        $("#verifyEmailBtn").text("이메일 인증 완료 ✅").css("background", "#4ade80");
        $("#registerBtn").prop("disabled", false);
    };
    
    
    // 이메일 인증 팝업 열기
	function openEmailPopup(email) {
	  if (!email) return;
	  const url = window.APP_CTX + '/email.user?email=' + encodeURIComponent(email);
	  window.open(url, 'emailVerify', 'width=480,height=400');
	};

	// 예: 버튼 클릭 시
	$('#btnEmailVerify').on('click', function () {
	  const email = $('#email').val();
	  openEmailPopup(email);
	});
    
});
