<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>비밀번호 재설정 완료</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <style>
        .success-container {
            background: #2a2a2a;
            border-radius: 12px;
            padding: 50px 40px;
            text-align: center;
            color: white;
            width: 90%;
            max-width: 420px;
            margin: 100px auto;
        }
        .success-icon { font-size: 60px; color: #4ade80; margin-bottom: 20px; }
        .btn-login {
            background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
            color: white;
            border: none;
            border-radius: 8px;
            padding: 12px 20px;
            font-size: 15px;
            cursor: pointer;
            transition: all 0.3s ease;
        }
        .btn-login:hover {
            background: linear-gradient(135deg, #3dd068 0%, #16a34a 100%);
            box-shadow: 0 4px 12px rgba(74, 222, 128, 0.3);
        }
    </style>
</head>
<body>
    <div class="success-container">
        <div class="success-icon">🔑</div>
        <h2>비밀번호가 성공적으로 변경되었습니다!</h2>
        <p>이제 새로운 비밀번호로 로그인할 수 있습니다.</p>
        <form action="${pageContext.request.contextPath}/login.user" method="get">
            <button type="submit" class="btn-login">로그인으로 이동</button>
        </form>
    </div>
</body>
</html>
