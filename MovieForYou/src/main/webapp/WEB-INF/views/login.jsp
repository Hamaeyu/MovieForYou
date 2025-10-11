<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/login.css">
</head>
<body>
    <div class="login-container">
        <div class="header">
            <div class="icon"><i class="fa-duotone fa-solid fa-user"></i></div>
            <div class="header-text">
                <h1>로그인</h1>
                <p>Sign in to your account</p>
            </div>
        </div>

        <form method="post" action="loginok" onsubmit="return validateForm()">
            <div class="form-group">
                <label for="username">Username or Email</label>
                <input type="text" id="username" name="username" placeholder="Enter your username" required>
            </div>

            <div class="form-group">
                <label for="password">Password</label>
                <input type="password" id="password" name="password" placeholder="Enter your password" required>
            </div>

            <div class="remember-forgot">
                <label>
                    <input type="checkbox" name="remember" value="on">
                    Remember me
                </label>
                <a href="forgotPassword.jsp">Forgot password?</a>
            </div>

            <div class="button-group">
                <button type="reset" class="btn-cancel">Cancel</button>
                <button type="submit" class="btn-login">Login</button>
            </div>
        </form>

        <div class="signup-link">
            Don't have an account? <a href="signup.jsp">Sign up here</a>
        </div>
    </div>

    <script>
        function validateForm() {
            const username = document.getElementById('username').value.trim();
            const password = document.getElementById('password').value.trim();

            if (!username) {
                alert('Please enter your username or email');
                return false;
            }

            if (!password) {
                alert('Please enter your password');
                return false;
            }

            if (password.length < 6) {
                alert('Password must be at least 6 characters');
                return false;
            }

            return true;
        }
    </script>
</body>
</html>