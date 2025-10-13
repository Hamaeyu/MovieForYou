<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<style>
  header {
    display: flex;
    justify-content: center;
    align-items: center;
    flex-direction: column;
    gap: 12px;
    margin-bottom: 40px;
    text-align: center;
  }

  .logo {
    display: flex;
    align-items: center;
    gap: 10px;
    justify-content: center;
  }

  .logo-icon {
    background-color: #ffd43b;
    width: 32px;
    height: 32px;
    border-radius: 8px;
    display: flex;
    align-items: center;
    justify-content: center;
    color: #000;
    font-weight: bold;
  }

  .logo h2 {
    font-size: 1.4rem;
    font-weight: 600;
  }

  nav {
    display: flex;
    gap: 30px;
    justify-content: center;
  }

  nav a {
    color: #bbb;
    text-decoration: none;
    font-weight: 500;
    transition: color 0.2s;
  }

  nav a.active {
    color: #ffd43b;
  }

  nav a:hover {
    color: #fff;
  }

  .admin-badge {
    background-color: #00cc66;
    color: #000;
    padding: 5px 12px;
    border-radius: 16px;
    font-size: 0.85rem;
    font-weight: 600;
    display: inline-block;
  }
</style>

<header>
  <div class="logo">
    <div class="logo-icon">C</div>
    <h2>CineForum - Admin</h2>
  </div>

  <nav>
    <a href="index.jsp">Home</a>
    <a href="reviews.jsp">Reviews</a>
    <a href="admin.jsp" class="active">Admin</a>
    <a href="mypage.jsp">My Page</a>
  </nav>
</header>
