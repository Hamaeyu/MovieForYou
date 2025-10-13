/**
 * login.jsp에 포함됨
 */

const inputEmail = document.getElementById("email");
const inputPw = document.getElementById("password");
const btnLogin = document.getElementById("login");
btnLogin.addEventListener("click", () => {
    // 공백 제거 후 검사
    if(inputEmail.value.trim() === "" || 
        inputPw.value.trim() === ""){
        alert("이메일 또는 비밀번호를 입력해주세요.");
        return;
    }
});