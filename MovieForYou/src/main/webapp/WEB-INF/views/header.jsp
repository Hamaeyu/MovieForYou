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
                  <a class="nav-item-a" href="/movieReview">영화관람후기</a>  
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
           <!-- 비 로그인 상태일 때 보이게 설정 -->
            <c:if test="${empty sessionScope.loginUser}">
                <a href="/login.auth" class="login-btn">로그인</a>
                <a href="" class="signup-btn">회원가입</a>
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