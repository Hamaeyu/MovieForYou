<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>500 - 서버 오류</title>
<style>
* {
    margin: 0;
    padding: 0;
    box-sizing: border-box;
}

body {
    background: linear-gradient(135deg, #1a1a1a 0%, #2d2d2d 100%);
    min-height: 100vh;
    display: flex;
    align-items: center;
    justify-content: center;
    font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
    overflow: hidden;
}

.error-container {
    text-align: center;
    color: #fff;
    position: relative;
    z-index: 10;
    padding: 40px;
}

.cat-container {
    position: relative;
    margin-bottom: 40px;
    animation: dizzy 1s ease-in-out infinite;
}

.cat {
    font-size: 120px;
    filter: drop-shadow(0 10px 30px rgba(255, 212, 59, 0.3));
}

.error-code {
    font-size: 100px;
    font-weight: 900;
    color: #ffd43b;
    text-shadow: 0 0 30px rgba(255, 212, 59, 0.5);
    margin-bottom: 20px;
    letter-spacing: 10px;
}

.error-title {
    font-size: 36px;
    font-weight: 700;
    margin-bottom: 20px;
    color: #fff;
}

.error-message {
    font-size: 18px;
    color: #b0b0b0;
    margin-bottom: 40px;
    line-height: 1.6;
}

.home-button {
    display: inline-block;
    padding: 16px 48px;
    background-color: #ffd43b;
    color: #1a1a1a;
    text-decoration: none;
    border-radius: 50px;
    font-size: 18px;
    font-weight: 600;
    transition: all 0.3s ease;
    box-shadow: 0 10px 30px rgba(255, 212, 59, 0.3);
}

.home-button:hover {
    background-color: #ffdd5e;
    transform: translateY(-3px);
    box-shadow: 0 15px 40px rgba(255, 212, 59, 0.5);
}

@keyframes dizzy {
    0%, 100% { transform: rotate(-5deg); }
    50% { transform: rotate(5deg); }
}

.paw-prints {
    position: absolute;
    width: 100%;
    height: 100%;
    top: 0;
    left: 0;
    opacity: 0.1;
    pointer-events: none;
}

.paw {
    position: absolute;
    font-size: 40px;
    animation: fadeInOut 4s infinite;
}

.paw:nth-child(1) { top: 10%; left: 10%; animation-delay: 0s; }
.paw:nth-child(2) { top: 20%; right: 15%; animation-delay: 1s; }
.paw:nth-child(3) { bottom: 20%; left: 20%; animation-delay: 2s; }
.paw:nth-child(4) { bottom: 15%; right: 25%; animation-delay: 3s; }

@keyframes fadeInOut {
    0%, 100% { opacity: 0; }
    50% { opacity: 0.3; }
}
</style>
</head>
<body>
<div class="paw-prints">
    <div class="paw">🐾</div>
    <div class="paw">🐾</div>
    <div class="paw">🐾</div>
    <div class="paw">🐾</div>
</div>
<div class="error-container">
    <div class="cat-container">
        <div class="cat">😵</div>
    </div>
    <div class="error-code">500</div>
    <h1 class="error-title">냥... 서버가 쓰러졌다냥!</h1>
    <p class="error-message">
        고양이가 서버를 고치는 중이에요.<br>
        잠시 후 다시 시도해주세요!
    </p>
    <a href="${pageContext.request.contextPath}/" class="home-button">🏠 메인으로 돌아가기</a>
</div>
</body>
</html>