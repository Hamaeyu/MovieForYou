<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            background: #1a1a1a;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, Oxygen, Ubuntu, Cantarell, sans-serif;
        }

        nav {
            background: #2d2d2d;
            padding: 0 30px;
            height: 70px;
            display: flex;
            align-items: center;
            justify-content: space-between;
            box-shadow: 0 2px 8px rgba(0, 0, 0, 0.3);
            position: sticky;
            top: 0;
            z-index: 1000;
            border-bottom: 2px solid #4CAF50;
        }

        .nav-logo {
            display: flex;
            align-items: center;
            gap: 12px;
            text-decoration: none;
            cursor: pointer;
        }

        .logo-icon {
            width: 40px;
            height: 40px;
            background: #4CAF50;
            border-radius: 6px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 22px;
            font-weight: 700;
            color: #ffffff;
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
        }

        .logo-text {
            font-size: 20px;
            font-weight: 700;
            color: #ffffff;
            letter-spacing: -0.5px;
        }

        .nav-menu {
            display: flex;
            gap: 40px;
            flex: 1;
            margin-left: 50px;
        }

        .nav-item {
            color: #b0b0b0;
            font-size: 14px;
            font-weight: 500;
            cursor: pointer;
            transition: all 0.3s ease;
            padding: 8px 12px;
            position: relative;
        }

        .nav-item:hover {
            color: #4CAF50;
        }

        .nav-item.active {
            color: #4CAF50;
        }

        .nav-item.active::after {
            content: '';
            position: absolute;
            bottom: -11px;
            left: 0;
            right: 0;
            height: 2px;
            background: #4CAF50;
        }

        .nav-right {
            display: flex;
            align-items: center;
            gap: 20px;
            margin-left: auto;
        }

        .user-info {
            display: flex;
            align-items: center;
            gap: 12px;
            cursor: pointer;
            transition: all 0.3s ease;
            padding: 8px 12px;
            border-radius: 6px;
        }

        .user-info:hover {
            background: rgba(76, 175, 80, 0.08);
        }

        .user-avatar {
            width: 36px;
            height: 36px;
            border-radius: 50%;
            background: #FFD700;
            display: flex;
            align-items: center;
            justify-content: center;
            color: #1a1a1a;
            font-weight: 700;
            font-size: 16px;
            box-shadow: 0 2px 8px rgba(255, 215, 0, 0.2);
        }

        .username {
            color: #ffffff;
            font-size: 13px;
            font-weight: 500;
        }

        .dropdown-icon {
            color: #4CAF50;
            font-size: 12px;
            transition: transform 0.3s ease;
        }

        .user-info:hover .dropdown-icon {
            transform: rotate(180deg);
        }

        .logout-btn {
            background: transparent;
            border: 1px solid #4CAF50;
            color: #4CAF50;
            padding: 8px 18px;
            border-radius: 4px;
            font-size: 13px;
            font-weight: 600;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .logout-btn:hover {
            background: #4CAF50;
            color: #ffffff;
            box-shadow: 0 4px 12px rgba(76, 175, 80, 0.2);
        }

        /* Dropdown Menu */
        .dropdown-menu {
            display: none;
            position: absolute;
            top: 70px;
            right: 180px;
            background: #3a3a3a;
            border-radius: 4px;
            min-width: 180px;
            box-shadow: 0 8px 24px rgba(0, 0, 0, 0.4);
            overflow: hidden;
            z-index: 999;
            border: 1px solid #404040;
        }

        .dropdown-menu.active {
            display: block;
            animation: slideDown 0.3s ease;
        }

        @keyframes slideDown {
            from {
                opacity: 0;
                transform: translateY(-10px);
            }
            to {
                opacity: 1;
                transform: translateY(0);
            }
        }

        .dropdown-item {
            padding: 12px 16px;
            color: #b0b0b0;
            font-size: 13px;
            cursor: pointer;
            transition: all 0.2s ease;
            border-bottom: 1px solid #404040;
        }

        .dropdown-item:last-child {
            border-bottom: none;
        }

        .dropdown-item:hover {
            background: rgba(76, 175, 80, 0.1);
            color: #4CAF50;
        }

        @media (max-width: 768px) {
            nav {
                padding: 0 15px;
                height: 60px;
            }

            .nav-logo {
                gap: 8px;
            }

            .logo-text {
                font-size: 18px;
            }

            .nav-menu {
                margin-left: 20px;
                gap: 20px;
            }

            .nav-item {
                font-size: 12px;
            }

            .nav-right {
                gap: 10px;
            }

            .username {
                display: none;
            }

            .logout-btn {
                padding: 6px 12px;
                font-size: 12px;
            }

            .dropdown-menu {
                right: 80px;
                min-width: 160px;
            }
        }
    </style>
</head>
<body>
    <nav>
        <div class="nav-logo">
            <div class="logo-icon">🎞️</div>
            <div class="logo-text">MovieForYou</div>
        </div>

        <div class="nav-menu">
            <div class="nav-item active" onclick="setActive(this)">영화
                소개</div>
            <div class="nav-item" onclick="setActive(this)">영화커뮤니티게시판</div>
            <div class="nav-item" onclick="setActive(this)">마이페이지</div>
        </div>

        <div class="nav-right">
            <div class="user-info" onclick="toggleDropdown()">
                <div class="user-avatar">JD</div>
                <span class="username">john_doe_2024</span> <span
                    class="dropdown-icon">▼</span>
            </div>

            <div class="dropdown-menu" id="dropdownMenu">
                <div class="dropdown-item">프로필 설정</div>
                <div class="dropdown-item">내 활동</div>
                <div class="dropdown-item">설정</div>
            </div>

            <button class="logout-btn">로그아웃</button>
        </div>
    </nav>

    <script>
        function toggleDropdown() {
            const menu = document.getElementById('dropdownMenu');
            menu.classList.toggle('active');
        }

        function setActive(element) {
            document.querySelectorAll('.nav-item').forEach(item => {
                item.classList.remove('active');
            });
            element.classList.add('active');
        }

        // 드롭다운 외부 클릭 시 닫기
        document.addEventListener('click', function(event) {
            const dropdown = document.getElementById('dropdownMenu');
            const userInfo = document.querySelector('.user-info');
            
            if (!userInfo.contains(event.target) && !dropdown.contains(event.target)) {
                dropdown.classList.remove('active');
            }
        });
    </script>
</body>
</html>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.4.2/css/all.min.css"/>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/header.css">
</head>
	<div class="nav-header">
	    <nav>
	        <div class="nav-logo">
	            <div class="logo-icon"><i class="fa-solid fa-film"></i></div>
	            <div class="logo-text">MovieForYou</div>
	        </div>
	
	        <div class="nav-menu">
	            <div class="nav-item active" onclick="setActive(this)">
	            	<a class="nav-item-a" href="">영화 소개</a>
	            </div>
	            <div class="nav-item" onclick="setActive(this)">
	            	<a class="nav-item-a" href="">영화관람후기</a>  
	            </div>
	            <div class="nav-item" onclick="setActive(this)">
	            	<a class="nav-item-a" href="">상영관 평가</a>
	            </div>
	            <div class="nav-item" onclick="setActive(this)">
	            	<a class="nav-item-a" href="">자유 게시판</a>
	            </div>
	        </div>
			<!-- 로그인 상태일 때만 보이게 설정 -->
			<c:if test="${not empty sessionScope.loginUser}">
	        <div class="nav-right">
	            <div class="user-info" onclick="toggleDropdown()">
	                <div class="user-avatar"><i class="fa-solid fa-user"></i></div>
	                <span class="username">${sessionScope.loginUserNickname}</span> <span
	                    class="dropdown-icon">▼</span>
	            </div>
	
	            <div class="dropdown-menu" id="dropdownMenu">
	                <div class="dropdown-item">프로필 설정</div>
	                <div class="dropdown-item">내 활동</div>
	                <div class="dropdown-item">설정</div>
	            </div>
	
	            <button class="logout-btn">로그아웃</button>
	        </div>
	        </c:if>
	    </nav>
    </div>
    <script>
        function toggleDropdown() {
            const menu = document.getElementById('dropdownMenu');
            menu.classList.toggle('active');
        }

        function setActive(element) {
            document.querySelectorAll('.nav-item').forEach(item => {
                item.classList.remove('active');
            });
            element.classList.add('active');
        }

        // 드롭다운 외부 클릭 시 닫기
        document.addEventListener('click', function(event) {
            const dropdown = document.getElementById('dropdownMenu');
            const userInfo = document.querySelector('.user-info');
            
            if (!userInfo.contains(event.target) && !dropdown.contains(event.target)) {
                dropdown.classList.remove('active');
            }
        });
    </script>
</html>