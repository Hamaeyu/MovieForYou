<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>에러 발생</title>
    <meta charset="UTF-8">
    <style>
        body { font-family: Arial, sans-serif; padding: 20px; background-color: #f8d7da; color: #721c24; }
        .error-box {
            border: 1px solid #f5c6cb;
            background-color: #f8d7da;
            padding: 20px;
            border-radius: 5px;
        }
        a {
            color: #721c24;
            text-decoration: underline;
        }
    </style>
</head>
<body>
    <div class="error-box">
        <h2>오류가 발생했습니다.</h2>
        <p>${errorMsg != null ? errorMsg : "알 수 없는 오류가 발생했습니다."}</p>
        <a href="${pageContext.request.contextPath}/signupForm.user">회원가입 페이지로 돌아가기</a>
    </div>
</body>
</html>
