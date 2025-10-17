<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>MovieForYou - 당신을 위한 영화</title>
<link rel="stylesheet"
	href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css" />
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/header.css">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/footer.css">
<style>
* {
	margin: 0;
	padding: 0;
	box-sizing: border-box;
}

body {
	font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto,
		sans-serif;
	background: #0a0a0a;
	color: #fff;
	overflow-x: hidden;
}

/* Hero Section */
.hero-section {
	position: relative;
	height: 700px;
	background: linear-gradient(180deg, rgba(10, 10, 10, 0.3) 0%,
		rgba(10, 10, 10, 0.9) 100%),
		url('https://images.unsplash.com/photo-1598899134739-24c46f58b8c0?w=1600')
		center/cover;
	display: flex;
	align-items: center;
	justify-content: center;
	overflow: hidden;
}

.hero-overlay {
	position: absolute;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	background: radial-gradient(circle at 50% 50%, transparent 0%, rgba(10, 10, 10, 0.8)
		100%);
}

.hero-content {
	position: relative;
	z-index: 2;
	text-align: center;
	padding: 40px;
	max-width: 900px;
}

.hero-icon {
	font-size: 100px;
	margin-bottom: 30px;
	animation: bounce 2s ease-in-out infinite;
}

@
keyframes bounce { 0%, 100% {
	transform: translateY(0);
}

50
%
{
transform
:
translateY(
-20px
);
}
}
.hero-title {
	font-size: 72px;
	font-weight: 900;
	margin-bottom: 20px;
	color: #ffd43b;
	text-shadow: 0 0 40px rgba(255, 212, 59, 0.5);
	letter-spacing: 2px;
}

.hero-subtitle {
	font-size: 24px;
	color: #ccc;
	margin-bottom: 50px;
	line-height: 1.6;
}

.hero-buttons {
	display: flex;
	gap: 20px;
	justify-content: center;
	flex-wrap: wrap;
}

.hero-btn {
	padding: 18px 45px;
	font-size: 18px;
	font-weight: 700;
	border-radius: 50px;
	text-decoration: none;
	transition: all 0.3s ease;
	display: inline-flex;
	align-items: center;
	gap: 12px;
}

.hero-btn-primary {
	background: #ffd43b;
	color: #000;
	box-shadow: 0 10px 40px rgba(255, 212, 59, 0.4);
}

.hero-btn-primary:hover {
	transform: translateY(-5px) scale(1.05);
	box-shadow: 0 15px 50px rgba(255, 212, 59, 0.6);
}

.hero-btn-secondary {
	background: rgba(255, 255, 255, 0.1);
	color: #fff;
	border: 2px solid rgba(255, 212, 59, 0.5);
	backdrop-filter: blur(10px);
}

.hero-btn-secondary:hover {
	background: rgba(255, 212, 59, 0.2);
	border-color: #ffd43b;
	transform: translateY(-5px);
}

/* Movie Slider */
.movie-slider-section {
	padding: 80px 0;
	background: #0a0a0a;
	overflow: hidden;
}

.section-header {
	text-align: center;
	margin-bottom: 60px;
}

.section-title {
	font-size: 48px;
	font-weight: 900;
	color: #fff;
	margin-bottom: 15px;
	position: relative;
	display: inline-block;
}

.section-title::after {
	content: '';
	position: absolute;
	bottom: -10px;
	left: 50%;
	transform: translateX(-50%);
	width: 100px;
	height: 4px;
	background: #ffd43b;
	border-radius: 2px;
}

.section-subtitle {
	font-size: 18px;
	color: #888;
	margin-top: 20px;
}

.slider-container {
	position: relative;
	width: 100%;
	overflow: hidden;
}

.slider-track {
	display: flex;
	animation: scroll 40s linear infinite;
}

.slider-track:hover {
	animation-play-state: paused;
}

@
keyframes scroll { 0% {
	transform: translateX(0);
}

100
%
{
transform
:
translateX(
-50%
);
}
}
.movie-card {
	flex: 0 0 280px;
	margin: 0 15px;
	position: relative;
	border-radius: 20px;
	overflow: hidden;
	transition: all 0.4s ease;
	cursor: pointer;
}

.movie-card:hover {
	transform: translateY(-15px) scale(1.05);
	box-shadow: 0 20px 60px rgba(255, 212, 59, 0.3);
}

.movie-poster {
	width: 100%;
	height: 400px;
	object-fit: cover;
	display: block;
}

.movie-overlay {
	position: absolute;
	bottom: 0;
	left: 0;
	right: 0;
	background: linear-gradient(to top, rgba(0, 0, 0, 0.95) 0%, transparent
		100%);
	padding: 30px 20px 20px;
	transform: translateY(10px);
	opacity: 0;
	transition: all 0.4s ease;
}

.movie-card:hover .movie-overlay {
	transform: translateY(0);
	opacity: 1;
}

.movie-title {
	font-size: 20px;
	font-weight: 700;
	color: #ffd43b;
	margin-bottom: 8px;
}

.movie-info {
	font-size: 14px;
	color: #ccc;
}

/* Features Grid */
.features-section {
	padding: 100px 20px;
	background: linear-gradient(135deg, #1a1a1a 0%, #0a0a0a 100%);
}

.features-grid {
	max-width: 1200px;
	margin: 0 auto;
	display: grid;
	grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
	gap: 40px;
}

.feature-card {
	background: rgba(255, 255, 255, 0.03);
	border: 2px solid rgba(255, 212, 59, 0.2);
	border-radius: 25px;
	padding: 50px 35px;
	text-align: center;
	transition: all 0.4s ease;
	position: relative;
	overflow: hidden;
}

.feature-card::before {
	content: '';
	position: absolute;
	top: -50%;
	left: -50%;
	width: 200%;
	height: 200%;
	background: radial-gradient(circle, rgba(255, 212, 59, 0.1) 0%,
		transparent 70%);
	opacity: 0;
	transition: opacity 0.4s ease;
}

.feature-card:hover::before {
	opacity: 1;
}

.feature-card:hover {
	transform: translateY(-10px);
	border-color: #ffd43b;
	box-shadow: 0 20px 50px rgba(255, 212, 59, 0.2);
}

.feature-icon {
	font-size: 70px;
	margin-bottom: 25px;
	color: #ffd43b;
	position: relative;
	z-index: 1;
}

.feature-title {
	font-size: 26px;
	font-weight: 700;
	color: #fff;
	margin-bottom: 15px;
	position: relative;
	z-index: 1;
}

.feature-description {
	font-size: 16px;
	color: #aaa;
	line-height: 1.7;
	position: relative;
	z-index: 1;
}

/* Popcorn Animation */
.popcorn-container {
	position: fixed;
	top: 0;
	left: 0;
	width: 100%;
	height: 100%;
	pointer-events: none;
	z-index: 1;
	overflow: hidden;
}

.popcorn {
	position: absolute;
	font-size: 40px;
	opacity: 0.15;
	animation: popcornFall 15s linear infinite;
}

.popcorn:nth-child(1) {
	left: 10%;
	animation-delay: 0s;
	animation-duration: 12s;
}

.popcorn:nth-child(2) {
	left: 25%;
	animation-delay: 3s;
	animation-duration: 15s;
}

.popcorn:nth-child(3) {
	left: 40%;
	animation-delay: 6s;
	animation-duration: 13s;
}

.popcorn:nth-child(4) {
	left: 55%;
	animation-delay: 2s;
	animation-duration: 14s;
}

.popcorn:nth-child(5) {
	left: 70%;
	animation-delay: 5s;
	animation-duration: 16s;
}

.popcorn:nth-child(6) {
	left: 85%;
	animation-delay: 4s;
	animation-duration: 11s;
}

@
keyframes popcornFall { 0% {
	top: -10%;
	transform: rotate(0deg);
}

100
%
{
top
:
110%;
transform
:
rotate(
720deg
);
}
}

/* CTA Section */
.cta-section {
	padding: 100px 20px;
	background: linear-gradient(135deg, #ffd43b 0%, #ffb300 100%);
	text-align: center;
	position: relative;
	overflow: hidden;
}

.cta-icon {
	font-size: 80px;
	margin-bottom: 30px;
	animation: rotate 10s linear infinite;
}

@
keyframes rotate {from { transform:rotate(0deg);
	
}

to {
	transform: rotate(360deg);
}

}
.cta-title {
	font-size: 48px;
	font-weight: 900;
	color: #000;
	margin-bottom: 20px;
}

.cta-text {
	font-size: 20px;
	color: #333;
	margin-bottom: 40px;
}

.cta-button {
	display: inline-block;
	padding: 20px 50px;
	background: #000;
	color: #ffd43b;
	font-size: 20px;
	font-weight: 700;
	border-radius: 50px;
	text-decoration: none;
	transition: all 0.3s ease;
	box-shadow: 0 10px 40px rgba(0, 0, 0, 0.3);
}

.cta-button:hover {
	transform: translateY(-5px) scale(1.05);
	box-shadow: 0 15px 50px rgba(0, 0, 0, 0.5);
}

@media ( max-width : 768px) {
	.hero-title {
		font-size: 48px;
	}
	.hero-subtitle {
		font-size: 18px;
	}
	.section-title {
		font-size: 36px;
	}
	.movie-card {
		flex: 0 0 220px;
	}
	.features-grid {
		grid-template-columns: 1fr;
	}
}
</style>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp" />

	<!-- Popcorn Animation -->
	<div class="popcorn-container">
		<div class="popcorn">🍿</div>
		<div class="popcorn">🍿</div>
		<div class="popcorn">🍿</div>
		<div class="popcorn">🍿</div>
		<div class="popcorn">🍿</div>
		<div class="popcorn">🍿</div>
	</div>

	<!-- Hero Section -->
	<section class="hero-section">
		<div class="hero-overlay"></div>
		<div class="hero-content">
			<div class="hero-icon">🎬</div>
			<h1 class="hero-title">MovieForYou</h1>
			<p class="hero-subtitle">
				당신을 위한 특별한 영화 경험<br> 지금 바로 영화의 세계로 빠져보세요
			</p>
			<div class="hero-buttons">
				<a href="/list.movie" class="hero-btn hero-btn-primary"> <i
					class="fa-solid fa-play"></i> 영화 탐색하기
				</a> <a href="/movieReview" class="hero-btn hero-btn-secondary"> <i
					class="fa-solid fa-star"></i> 리뷰 보기
				</a>
			</div>
		</div>
	</section>

	<!-- Movie Slider -->
	<section class="movie-slider-section">
		<div class="section-header">
			<h2 class="section-title">🎥 인기 영화</h2>
			<p class="section-subtitle">지금 가장 핫한 영화들을 만나보세요</p>
		</div>
		<div class="slider-container">
			<div class="slider-track">
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">액션 블록버스터</div>
						<div class="movie-info">⭐ 9.2 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1594908900066-3f47337549d8?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">스릴러 명작</div>
						<div class="movie-info">⭐ 8.9 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1485846234645-a62644f84728?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">로맨틱 코미디</div>
						<div class="movie-info">⭐ 8.5 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">SF 대작</div>
						<div class="movie-info">⭐ 9.1 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">드라마 걸작</div>
						<div class="movie-info">⭐ 8.8 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">호러 스릴러</div>
						<div class="movie-info">⭐ 8.3 • 2024</div>
					</div>
				</div>
				<!-- Duplicate for seamless loop -->
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1536440136628-849c177e76a1?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">액션 블록버스터</div>
						<div class="movie-info">⭐ 9.2 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1594908900066-3f47337549d8?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">스릴러 명작</div>
						<div class="movie-info">⭐ 8.9 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1485846234645-a62644f84728?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">로맨틱 코미디</div>
						<div class="movie-info">⭐ 8.5 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1489599849927-2ee91cede3ba?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">SF 대작</div>
						<div class="movie-info">⭐ 9.1 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1478720568477-152d9b164e26?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">드라마 걸작</div>
						<div class="movie-info">⭐ 8.8 • 2024</div>
					</div>
				</div>
				<div class="movie-card">
					<img
						src="https://images.unsplash.com/photo-1440404653325-ab127d49abc1?w=400"
						alt="Movie" class="movie-poster">
					<div class="movie-overlay">
						<div class="movie-title">호러 스릴러</div>
						<div class="movie-info">⭐ 8.3 • 2024</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<!-- Features -->
	<section class="features-section">
		<div class="section-header">
			<h2 class="section-title">✨ MovieForYou의 특별함</h2>
			<p class="section-subtitle">왜 우리 커뮤니티를 선택해야 할까요?</p>
		</div>
		<div class="features-grid">
			<div class="feature-card">
				<div class="feature-icon">🎬</div>
				<h3 class="feature-title">다양한 영화 정보</h3>
				<p class="feature-description">최신 개봉작부터 클래식 명작까지, 모든 영화 정보를 한곳에서</p>
			</div>
			<div class="feature-card">
				<div class="feature-icon">⭐</div>
				<h3 class="feature-title">진솔한 리뷰</h3>
				<p class="feature-description">실제 관람객들의 생생한 후기와 평가를 확인하세요</p>
			</div>
			<div class="feature-card">
				<div class="feature-icon">🎭</div>
				<h3 class="feature-title">상영관 평가</h3>
				<p class="feature-description">전국 영화관의 시설과 서비스를 비교하고 선택하세요</p>
			</div>
			<div class="feature-card">
				<div class="feature-icon">💬</div>
				<h3 class="feature-title">활발한 커뮤니티</h3>
				<p class="feature-description">영화를 사랑하는 사람들과 자유롭게 소통하세요</p>
			</div>
		</div>
	</section>

	<!-- CTA Section -->
	<section class="cta-section">
		<div class="cta-icon">🎥</div>
		<h2 class="cta-title">지금 바로 시작하세요!</h2>
		<p class="cta-text">MovieForYou와 함께 특별한 영화 경험을 만들어보세요</p>
		<a href="/list.movie" class="cta-button">영화 둘러보기 →</a>
	</section>

	<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</body>
</html>