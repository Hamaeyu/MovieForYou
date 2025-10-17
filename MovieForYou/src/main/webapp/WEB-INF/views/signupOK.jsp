<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>회원가입 완료</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/signup.css">
    <style>
        body {
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
            background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
            color: #fff;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
        }

        .success-container {
            background: #2a2a2a;
            padding: 50px 40px;
            border-radius: 12px;
            text-align: center;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
            max-width: 420px;
            width: 90%;
        }

        .success-icon {
            font-size: 60px;
            color: #4ade80;
            margin-bottom: 20px;
        }

        h2 {
            font-size: 24px;
            margin-bottom: 10px;
            color: #fff;
        }

        p {
            font-size: 15px;
            color: #bbb;
            margin-bottom: 30px;
        }

        .btn-login {
            background: linear-gradient(135deg, #4ade80 0%, #22c55e 100%);
            border: none;
            padding: 12px 24px;
            border-radius: 8px;
            color: white;
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
        <div class="success-icon">✅</div>
        <h2>회원가입이 완료되었습니다!</h2>
        <p>🎞Movie For You에 오신 것을 환영합니다    로그인 후 서비스를 이용해주세요.</p>

        <form action="${pageContext.request.contextPath}/login.user" method="get">
            <button type="submit" class="btn-login">로그인 페이지로 이동</button>
        </form>
    </div>
</body>
</html>
