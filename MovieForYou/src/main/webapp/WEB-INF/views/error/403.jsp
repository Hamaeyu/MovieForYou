<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>403 - 접근 금지</title>
<style>
* {
margin: 0;
padding: 0;
box-sizing: border-box;
}

body {
background: linear-gradient(135deg, #0f0c29 0%, #302b63 50%, #24243e 100%);
min-height: 100vh;
display: flex;
align-items: center;
justify-content: center;
font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
overflow: hidden;
position: relative;
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
animation: guardFloat 2s ease-in-out infinite;
}

.cat {
font-size: 120px;
filter: drop-shadow(0 10px 30px rgba(239, 68, 68, 0.4));
display: inline-block;
animation: guardShake 0.5s ease-in-out infinite alternate;
}

.shield {
position: absolute;
font-size: 80px;
top: 50%;
left: 50%;
transform: translate(-50%, -50%);
animation: shieldPulse 2s ease-in-out infinite;
z-index: -1;
}

.error-code {
font-size: 100px;
font-weight: 900;
background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
-webkit-background-clip: text;
-webkit-text-fill-color: transparent;
background-clip: text;
text-shadow: 0 0 40px rgba(239, 68, 68, 0.5);
margin-bottom: 20px;
letter-spacing: 10px;
animation: glowPulse 2s ease-in-out infinite;
}

.error-title {
font-size: 36px;
font-weight: 700;
margin-bottom: 20px;
color: #fff;
text-shadow: 0 0 20px rgba(255, 255, 255, 0.3);
}

.error-message {
font-size: 18px;
color: #b0b0b0;
margin-bottom: 40px;
line-height: 1.6;
max-width: 500px;
margin-left: auto;
margin-right: auto;
}

.home-button {
display: inline-block;
padding: 16px 48px;
background: linear-gradient(135deg, #ef4444 0%, #dc2626 100%);
color: #fff;
text-decoration: none;
border-radius: 50px;
font-size: 18px;
font-weight: 600;
transition: all 0.3s ease;
box-shadow: 0 10px 30px rgba(239, 68, 68, 0.4);
border: 2px solid rgba(255, 255, 255, 0.1);
}

.home-button:hover {
background: linear-gradient(135deg, #dc2626 0%, #b91c1c 100%);
transform: translateY(-3px);
box-shadow: 0 15px 40px rgba(239, 68, 68, 0.6);
border-color: rgba(255, 255, 255, 0.3);
}

@keyframes guardFloat {
0%, 100% { transform: translateY(0px); }
50% { transform: translateY(-15px); }
}

@keyframes guardShake {
0% { transform: rotate(-2deg); }
100% { transform: rotate(2deg); }
}

@keyframes shieldPulse {
0%, 100% { 
opacity: 0.3;
transform: translate(-50%, -50%) scale(1);
}
50% { 
opacity: 0.6;
transform: translate(-50%, -50%) scale(1.1);
}
}

@keyframes glowPulse {
0%, 100% { 
text-shadow: 0 0 40px rgba(239, 68, 68, 0.5);
}
50% { 
text-shadow: 0 0 60px rgba(239, 68, 68, 0.8);
}
}

.warning-signs {
position: absolute;
width: 100%;
height: 100%;
top: 0;
left: 0;
opacity: 0.08;
pointer-events: none;
}

.warning {
position: absolute;
font-size: 50px;
animation: warningFloat 6s infinite;
}

.warning:nth-child(1) { 
top: 10%; 
left: 10%; 
animation-delay: 0s; 
animation-duration: 7s;
}

.warning:nth-child(2) { 
top: 20%; 
right: 15%; 
animation-delay: 1.5s; 
animation-duration: 6s;
}

.warning:nth-child(3) { 
bottom: 20%; 
left: 20%; 
animation-delay: 3s; 
animation-duration: 8s;
}

.warning:nth-child(4) { 
bottom: 15%; 
right: 25%; 
animation-delay: 4.5s; 
animation-duration: 7s;
}

.warning:nth-child(5) { 
top: 50%; 
left: 5%; 
animation-delay: 2s; 
animation-duration: 9s;
}

.warning:nth-child(6) { 
top: 60%; 
right: 10%; 
animation-delay: 5s; 
animation-duration: 6.5s;
}

@keyframes warningFloat {
0% { 
opacity: 0; 
transform: translateY(0) rotate(0deg);
}
50% { 
opacity: 0.15;
transform: translateY(-30px) rotate(180deg);
}
100% { 
opacity: 0;
transform: translateY(-60px) rotate(360deg);
}
}

.particles {
position: absolute;
width: 100%;
height: 100%;
top: 0;
left: 0;
pointer-events: none;
}

.particle {
position: absolute;
width: 3px;
height: 3px;
background: #ef4444;
border-radius: 50%;
opacity: 0;
animation: particleRise 4s infinite;
}

.particle:nth-child(1) { left: 20%; animation-delay: 0s; }
.particle:nth-child(2) { left: 40%; animation-delay: 1s; }
.particle:nth-child(3) { left: 60%; animation-delay: 2s; }
.particle:nth-child(4) { left: 80%; animation-delay: 3s; }
.particle:nth-child(5) { left: 30%; animation-delay: 0.5s; }
.particle:nth-child(6) { left: 50%; animation-delay: 1.5s; }
.particle:nth-child(7) { left: 70%; animation-delay: 2.5s; }
.particle:nth-child(8) { left: 90%; animation-delay: 3.5s; }

@keyframes particleRise {
0% {
bottom: 0;
opacity: 0;
}
50% {
opacity: 0.5;
}
100% {
bottom: 100%;
opacity: 0;
}
}
</style>
</head>
<body>
<div class="warning-signs">
<div class="warning">⚠️</div>
<div class="warning">⚠️</div>
<div class="warning">⚠️</div>
<div class="warning">⚠️</div>
<div class="warning">⚠️</div>
<div class="warning">⚠️</div>
</div>
<div class="particles">
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
<div class="particle"></div>
</div>
<div class="error-container">
<div class="cat-container">
<div class="shield">🛡️</div>
<div class="cat">😾</div>
</div>
<div class="error-code">403</div>
<h1 class="error-title">냐옹! 여기는 출입금지다냥!</h1>
<p class="error-message">
이 영역은 고양이 경비원이 지키고 있어요.<br>
접근 권한이 없어서 들어갈 수 없습니다.<br>
관리자에게 권한을 요청해주세요!
</p>
<a href="${pageContext.request.contextPath}/" class="home-button">🏠 안전한 곳으로 돌아가기</a>
</div>
</body>
</html>