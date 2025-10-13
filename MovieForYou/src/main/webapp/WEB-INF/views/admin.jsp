<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
  <meta charset="UTF-8">
  <title>Admin Dashboard</title>
  <style>
    body {
      background-color: #111;
      color: #fff;
      padding: 30px;
      font-family: 'Inter', sans-serif;
    }

    h1 {
      font-size: 1.5rem;
      margin-bottom: 20px;
    }

    .dashboard {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
    }

    .card {
      background-color: #1c1c1c;
      border-radius: 12px;
      padding: 20px;
      box-shadow: 0 0 4px rgba(0,0,0,0.2);
    }

    .metric {
      display: flex;
      align-items: center;
      justify-content: space-between;
    }

    .metric-icon {
      background-color: #222;
      border-radius: 50%;
      padding: 10px;
      font-size: 1.2rem;
    }

    .card h2 {
      font-size: 2rem;
      margin-bottom: 5px;
    }

    .card small {
      color: #999;
    }

    .green { color: #00e676; }
    .yellow { color: #ffd43b; }
    .orange { color: #ff9800; }
    .pink { color: #ff4081; }

    .section {
      display: flex;
      flex-wrap: wrap;
      gap: 20px;
    }

    .user-management, .popular {
      flex: 1 1 45%;
      background-color: #1c1c1c;
      border-radius: 12px;
      padding: 20px;
    }

    .user-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      margin-bottom: 15px;
    }

    .user-header select, .user-header button {
      background-color: #2a2a2a;
      border: none;
      color: #fff;
      padding: 8px 12px;
      border-radius: 8px;
      cursor: pointer;
    }

    .user-list {
      display: flex;
      flex-direction: column;
      gap: 12px;
    }

    .user {
      display: flex;
      align-items: center;
      justify-content: space-between;
      background-color: #2a2a2a;
      padding: 10px 14px;
      border-radius: 8px;
    }

    .user-info {
      display: flex;
      align-items: center;
      gap: 10px;
    }

    .user-avatar {
      width: 36px;
      height: 36px;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-weight: bold;
    }

    .user-avatar.green { background-color: #00e676; }
    .user-avatar.orange { background-color: #ff9800; }
    .user-avatar.pink { background-color: #ff4081; }

    .user-email {
      color: #aaa;
      font-size: 0.9rem;
    }

    .user-status {
      background-color: #00cc66;
      color: #000;
      padding: 4px 10px;
      border-radius: 12px;
      font-size: 0.8rem;
    }

    .inactive { background-color: #ff4444; color: #fff; }

    .user-actions button {
      background: none;
      border: none;
      color: #ccc;
      font-size: 1rem;
      cursor: pointer;
      margin-left: 8px;
    }

    .popular h3 {
      margin-bottom: 10px;
    }

    .chart {
      background-color: #2a2a2a;
      border-radius: 8px;
      height: 120px;
      margin-bottom: 15px;
      display: flex;
      align-items: flex-end;
      justify-content: space-evenly;
      padding: 10px;
    }

    .bar {
      width: 20px;
      border-radius: 4px 4px 0 0;
    }

    .bar.green { background-color: #00e676; height: 60px; }
    .bar.yellow { background-color: #ffd43b; height: 90px; }
    .bar.orange { background-color: #ff9800; height: 80px; }
    .bar.blue { background-color: #2196f3; height: 70px; }

    .movie-list {
      list-style: none;
    }

    .movie-list li {
      display: flex;
      justify-content: space-between;
      color: #ccc;
      padding: 4px 0;
    }

    .movie-list span {
      color: #00e676;
    }

    @media (max-width: 768px) {
      .section { flex-direction: column; }
    }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/views/header.jsp"/>

  <h1>Admin Dashboard</h1>

  <div class="dashboard">
    <div class="card">
      <div class="metric">
        <h2><%= request.getAttribute("totalUser") %></h2>
        <div class="metric-icon green">👥</div>
      </div>
      <small>+12.5% from last month</small><br>
      <small>Total Users</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2>247</h2>
        <div class="metric-icon yellow">📝</div>
      </div>
      <small>+8.3% from yesterday</small><br>
      <small>Daily Signups</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2>8,924</h2>
        <div class="metric-icon orange">📄</div>
      </div>
      <small>+15.2% this week</small><br>
      <small>Total Posts</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2>1,456</h2>
        <div class="metric-icon pink">⭐</div>
      </div>
      <small>-5.7% today</small><br>
      <small>Active Reviews</small>
    </div>
  </div>

  <div class="section">
    <div class="user-management">
      <div class="user-header">
        <h3>User Management</h3>
        <div>
          <select>
            <option>All Users</option>
          </select>
          <button>+ Add User</button>
        </div>
      </div>

      <div class="user-list">
        <div class="user">
          <div class="user-info">
            <div class="user-avatar green">J</div>
            <div>
              <strong>John Doe</strong><br>
              <span class="user-email">john.doe@email.com</span><br>
              <small>247 posts · Joined: Jan 2024</small>
            </div>
          </div>
          <div>
            <span class="user-status">Active</span>
            <div class="user-actions">
              <button>✏️</button>
              <button>🗑️</button>
            </div>
          </div>
        </div>

        <div class="user">
          <div class="user-info">
            <div class="user-avatar orange">S</div>
            <div>
              <strong>Sarah Wilson</strong><br>
              <span class="user-email">sarah.wilson@email.com</span><br>
              <small>89 posts · Joined: Feb 2024</small>
            </div>
          </div>
          <div>
            <span class="user-status inactive">Inactive</span>
            <div class="user-actions">
              <button>✏️</button>
              <button>🗑️</button>
            </div>
          </div>
        </div>

        <div class="user">
          <div class="user-info">
            <div class="user-avatar pink">M</div>
            <div>
              <strong>Mike Johnson</strong><br>
              <span class="user-email">mike.j@email.com</span><br>
              <small>156 posts · Joined: Mar 2024</small>
            </div>
          </div>
          <div>
            <span class="user-status">Active</span>
            <div class="user-actions">
              <button>✏️</button>
              <button>🗑️</button>
            </div>
          </div>
        </div>
      </div>
    </div>

    <div class="popular">
      <h3>Popular Movies & Posts</h3>
      <div class="chart">
        <div class="bar green"></div>
        <div class="bar yellow"></div>
        <div class="bar orange"></div>
        <div class="bar blue"></div>
      </div>
      <ul class="movie-list">
        <li>The Dark Knight <span>2.3M views</span></li>
        <li>Inception <span>1.8M views</span></li>
        <li>The Godfather <span>2.1M views</span></li>
      </ul>
    </div>
  </div>

</body>
</html>
