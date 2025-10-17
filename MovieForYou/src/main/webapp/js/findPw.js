	
	$(document).ready(function() { 
	
	let emailOK=false, codeOK=false, pwOK=false, pwMatch=false;
	
	// 이메일 + 닉네임 확인
	$("#nickname, #email").on("input", function(){
	    const email=$("#email").val();
	    const nickname=$("#nickname").val();
	    if(email && nickname){
	        $.post(window.APP_CTX + "/findPw.user", { email, nickname })
			  .done(function(res) {
			    if (res === "FOUND") {
			      $("#checkMsg").text("회원 정보가 확인되었습니다.").css("color", "green");
			      $("#sendCodeBtn").prop("disabled", false);
			    } else {
			      $("#checkMsg").text("입력하신 정보가 일치하지 않습니다.").css("color", "red");
			      $("#sendCodeBtn").prop("disabled", true);
			    }
			  })
			  .fail(function() {
			    alert("서버 연결에 실패했습니다. 잠시 후 다시 시도해주세요.");
			  });
	    }
	});
	
	// 인증번호 발송
	$("#sendCodeBtn").on("click", function(){
	    const email=$("#email").val();
	   $.post(window.APP_CTX + "/sendVerifyCode.user", { email })
		  .done(function(res) {
		    if (res === "SENT") {
		      $("#verifyArea").show();
		      $("#verifyMsg").text("인증번호가 전송되었습니다.").css("color", "lightgreen");
		    } else {
		      $("#verifyMsg").text("메일 전송 실패").css("color", "red");
		    }
		  })
		  .fail(function() {
		    alert("이메일 인증 서버에 일시적인 문제가 있습니다. 다시 시도해주세요.");
		  });
		});
	
	// 인증번호 확인
	$("#checkCodeBtn").on("click", function(){
	    const code=$("#verifyCode").val();
	    $.post(window.APP_CTX + "/verifyCode.user",{code},function(res){
	        if(res==="OK"){
	            $("#verifyMsg").text("인증되었습니다!").css("color","green");
	            codeOK=true;
	            $("#newPw").prop("disabled",false);
	        }else if(res==="EXPIRED"){
	            $("#verifyMsg").text("인증번호가 만료되었습니다. 다시 시도해주세요.").css("color","red");
	        }else{
	            $("#verifyMsg").text("인증번호가 일치하지 않습니다.").css("color","red");
	        }
	    });
	});
	
	// 새 비밀번호 유효성 검사
	$("#newPw").on("input", function(){
	    const pw=$(this).val();
	    const valid=/^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*?&]).{5,20}$/;
	    if(valid.test(pw)){
	        $("#pwMsg").text("사용 가능한 비밀번호입니다.").css("color","green");
	        pwOK=true;
	        $("#confirmPw").prop("disabled",false);
	    }else{
	        $("#pwMsg").text("영문자, 숫자, 특수문자를 포함 5~20자").css("color","red");
	        pwOK=false;
	    }
	});
	
	// 비밀번호 일치 확인
	$("#confirmPw").on("input", function(){
	    const pw=$("#newPw").val();
	    const confirm=$(this).val();
	    if(pw===confirm){
	        $("#confirmMsg").text("비밀번호가 일치합니다.").css("color","green");
	        pwMatch=true;
	        $("#resetBtn").prop("disabled",false);
	    }else{
	        $("#confirmMsg").text("비밀번호가 일치하지 않습니다.").css("color","red");
	        pwMatch=false;
	        $("#resetBtn").prop("disabled",true);
	    }
	});
	
	// 비밀번호 재설정 Ajax
	$("#resetBtn").on("click", function(){
	    const email=$("#email").val();
	    const password=$("#newPw").val();
	
	    $.post(window.APP_CTX + "/resetPw.user", { email, password })
		  .done(function(res) {
		    if (res === "SUCCESS") {
		      $("#findPw-container").fadeOut(300, function() {
		        $(this)
		          .html("<h2>비밀번호가 성공적으로 변경되었습니다 🎉</h2><p>3초 후 로그인 페이지로 이동합니다...</p>")
		          .fadeIn(400);
		      });
		      setTimeout(() => {
		        window.location.href = window.APP_CTX + "/login.auth";
		      }, 3000);
		    } else {
		      alert("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
		    }
		  })
		  .fail(function() {
		    alert("서버 통신 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
		  });
	});
	
	
	// 타이머 변수 전역 선언
	// 실시간 입력 시 서버 부화 최소화
	let debounceTimer; 
	
	$("#nickname, #email").on("input", function() {
	  clearTimeout(debounceTimer); // 타이핑 중이면 이전 타이머 취소
	  debounceTimer = setTimeout(function() {
	    const email = $("#email").val();
	    const nickname = $("#nickname").val();
	
	    if (email && nickname) {
	      $.post(window.APP_CTX + "/findPw.user", { email, nickname }, function(res) {
	        if (res === "FOUND") {
	          $("#checkMsg").text("회원 정보가 확인되었습니다.").css("color", "green");
	          $("#sendCodeBtn").prop("disabled", false);
	        } else {
	          $("#checkMsg").text("입력하신 정보가 일치하지 않습니다.").css("color", "red");
	          $("#sendCodeBtn").prop("disabled", true);
	        }
	      });
	    }
	  }, 400); // 입력 멈춘 후 0.4초 뒤 실행
	});

	//버튼 더블클릭 방지 >> 비밀번호 재설정 버튼, 한 번 누르면 비활성화
	$("#resetBtn").on("click", function() {
	  const $btn = $(this);
	  $btn.prop("disabled", true); // 버튼 비활성화
	
	  const email = $("#email").val();
	  const password = $("#newPw").val();
	
	  $.post(window.APP_CTX + "/resetPw.user", { email, password })
	    .done(function(res) {
	      if (res === "SUCCESS") {
	        $("#findPw-container").fadeOut(300, function() {
	          $(this).html("<h2>비밀번호가 성공적으로 변경되었습니다 🎉</h2><p>3초 후 로그인 페이지로 이동합니다...</p>").fadeIn(400);
	        });
	        setTimeout(() => {
	          window.location.href = window.APP_CTX + "/login.auth";
	        }, 3000);
	      } else {
	        alert("비밀번호 변경에 실패했습니다. 다시 시도해주세요.");
	        $btn.prop("disabled", false); // 실패 시 버튼 다시 활성화
	      }
	    })
	    .fail(function() {
	      alert("서버 연결 오류입니다. 다시 시도해주세요.");
	      $btn.prop("disabled", false);
	    });
	});

	
	
	
});
