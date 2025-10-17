$(document).ready(function() { 
	
	let emailOK = false, codeOK = false, pwOK = false, pwMatch = false, nickOK = false;

// 이메일 중복확인
$("#checkEmailBtn").on("click", function() {
  const email = $("#email").val();
  if (!email) return;

  $.post(window.APP_CTX + "/checkEmail.user", { email }, function(res) {
    if (res === "EXISTS") {
      $("#emailMsg").text("중복된 이메일입니다.").css("color", "red");
      emailOK = false;
      $("#sendCodeBtn").prop("disabled", true).addClass("disabled");
    } else {
      $("#emailMsg").text("사용 가능한 이메일입니다.").css("color", "green");
      emailOK = true;
      $("#sendCodeBtn").prop("disabled", false).removeClass("disabled");
    }
  });
});

// 이메일 인증번호 전송
$("#sendCodeBtn").on("click", function() {
  const email = $("#email").val();
  $.post(window.APP_CTX + "/sendVerifyCode.user", { email }, function(res) {
    if (res === "SENT") {
      $("#verifyArea").show();
      $("#verifyMsg").text("인증번호가 전송되었습니다.").css("color", "lightgreen");
    } else {
      $("#verifyMsg").text("메일 전송 실패").css("color", "red");
    }
  });
});

// 인증번호 확인
$("#checkCodeBtn").on("click", function() {
  const code = $("#verifyCode").val();
  $.post(window.APP_CTX + "/verifyCode.user", { code }, function(res) {
    if (res === "OK") {
      $("#verifyMsg").text("인증되었습니다!").css("color", "green");
      codeOK = true;
      $("#password").prop("disabled", false);
    } else if (res === "EXPIRED") {
      $("#verifyMsg").text("인증번호가 만료되었습니다. 다시 시도해주세요.").css("color", "red");
    } else {
      $("#verifyMsg").text("인증번호가 일치하지 않습니다.").css("color", "red");
    }
  });
});

// 비밀번호 검증
$("#password").on("input", function() {
  const pw = $(this).val();
  const valid = /^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&]).{5,20}$/;
  if (valid.test(pw)) {
    $("#passwordMsg").text("가능한 비밀번호입니다.").css("color", "green");
    pwOK = true;
    $("#confirmPw").prop("disabled", false);
  } else {
    $("#passwordMsg").text("영문자, 숫자, 특수문자를 포함 5~20자").css("color", "red");
    pwOK = false;
  }
});

// 비밀번호 확인
$("#confirmPw").on("input", function() {
  const pw = $("#password").val();
  const confirm = $(this).val();
  if (pw === confirm) {
    $("#confirmMsg").text("비밀번호가 일치합니다.").css("color", "green");
    pwMatch = true;
    $("#nickname").prop("disabled", false);
  } else {
    $("#confirmMsg").text("비밀번호가 일치하지 않습니다.").css("color", "red");
    pwMatch = false;
  }
});

// 닉네임 중복확인
$("#checkNickBtn").on("click", function() {
  const nickname = $("#nickname").val();
  $.post(window.APP_CTX + "/checkNickname.user", { nickname }, function(res) {
    if (res === "EXISTS") {
      $("#nickMsg").text("중복된 닉네임입니다.").css("color", "red");
      nickOK = false;
    } else {
      $("#nickMsg").text("사용 가능한 닉네임입니다.").css("color", "green");
      nickOK = true;
      if (emailOK && codeOK && pwOK && pwMatch && nickOK) {
        $("#signupBtn").prop("disabled", false);
      }
    }
  });
});

// ✅ 회원가입 Ajax 처리
$("#signupBtn").on("click", function() {
  const formData = {
    email: $("#email").val(),
    password: $("#password").val(),
    nickname: $("#nickname").val()
  };

  $.post(window.APP_CTX + "/signup.user", formData, function(res) {
    // 서버가 signupOK.jsp를 forward하면 HTML이 응답으로 옴
    $("#signup-container").fadeOut(300, function() {
      $(this).html(res).fadeIn(400);
    });

    // 3초 후 메인페이지로 이동
    setTimeout(() => {
      window.location.href = window.APP_CTX + "/main.user";
    }, 3000);
  }).fail(function() {
    alert("회원가입 처리 중 오류가 발생했습니다.");
	  });
	 });
  });