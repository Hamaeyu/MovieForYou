<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ include file="/WEB-INF/views/header.jsp" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>CineForum | Review Detail</title>
<style>
  body {
    background-color: #0d0d0d;
    color: #e5e5e5;
    font-family: "Inter", sans-serif;
    margin: 0;
    padding: 0;
  }

  .container {
    max-width: 1100px;
    margin: 50px auto;
    padding: 0 20px;
    display: flex;
    gap: 20px;
  }

  /* 메인 콘텐츠 */
  .main-content {
    flex: 3;
    background-color: #1a1a1a;
    border-radius: 12px;
    padding: 30px;
    box-shadow: 0 0 15px rgba(0,0,0,0.4);
  }

  .post-header h1 {
    color: #fff;
    font-size: 1.8rem;
    margin-bottom: 5px;
  }

  .author-info {
    display: flex;
    align-items: center;
    gap: 10px;
    color: #a3a3a3;
    margin-bottom: 20px;
  }

  .author-info img {
    width: 36px;
    height: 36px;
    border-radius: 50%;
  }

  .post-stats {
    color: #777;
    font-size: 0.9rem;
    margin-bottom: 15px;
  }

  .post-actions button {
    background-color: #2e2e2e;
    border: none;
    border-radius: 6px;
    color: #e5e5e5;
    padding: 8px 16px;
    cursor: pointer;
    margin-right: 8px;
  }

  .post-actions .edit { background-color: #facc15; color: #000; }
  .post-actions .delete { background-color: #ef4444; }

  .post-body {
    line-height: 1.6;
    margin-top: 20px;
  }

  /* Related Movie */
  .related-movie {
    background-color: #262626;
    border-radius: 10px;
    padding: 20px;
    margin-top: 30px;
  }

  .related-movie h3 {
    margin-top: 0;
    color: #fff;
  }

  .movie-card {
    display: flex;
    align-items: center;
    gap: 20px;
  }

  .movie-thumbnail {
    width: 60px;
    height: 60px;
    background-color: #3a3a3a;
    border-radius: 6px;
  }

  .movie-info {
    color: #ccc;
  }

  .rating {
    color: #facc15;
    font-weight: bold;
  }

  /* Comments Section */
  .comments {
    margin-top: 40px;
  }

  .comment-box {
    background-color: #2a2a2a;
    border-radius: 10px;
    padding: 15px;
    margin-bottom: 10px;
  }

  .comment-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
  }

  .comment-author {
    display: flex;
    align-items: center;
    gap: 10px;
  }

  .comment-author span {
    color: #fff;
    font-weight: 500;
  }

  .comment-text {
    margin-top: 10px;
    color: #ccc;
  }

  .comment-meta {
    font-size: 0.85rem;
    color: #777;
    margin-top: 5px;
  }

  .comment-actions {
    display: flex;
    gap: 10px;
    margin-top: 10px;
  }

  .comment-actions button {
    background: none;
    border: none;
    color: #facc15;
    cursor: pointer;
  }

  /* 사이드바 */
  .sidebar {
    flex: 1;
    display: flex;
    flex-direction: column;
    gap: 20px;
  }

  .sidebar-section {
    background-color: #1a1a1a;
    border-radius: 12px;
    padding: 20px;
  }

  .sidebar-section h3 {
    margin-bottom: 10px;
    color: #fff;
  }

  .related-post {
    margin-bottom: 10px;
  }

  .related-post a {
    color: #facc15;
    text-decoration: none;
  }

  .related-post small {
    color: #777;
  }
</style>
</head>

<body>
  <div class="container">
    <div class="main-content">
      <div class="post-header">
        <h1>The Dark Knight: A Masterpiece of Modern Cinema</h1>
        <div class="author-info">
          <img src="https://i.pravatar.cc/36" alt="John Doe">
          <span>John Doe</span> · November 25, 2024
        </div>
        <div class="post-stats">
          1,234 views · 156 likes · 23 comments
        </div>
        <div class="post-actions">
          <button class="edit">Edit</button>
          <button class="delete">Delete</button>
        </div>
      </div>

      <div class="post-body">
        <p>
          Christopher Nolan’s "The Dark Knight" stands as one of the greatest superhero films ever made, transcending the genre to become a masterpiece of modern cinema.
        </p>
        <p>
          Heath Ledger's portrayal of the Joker is nothing short of legendary. His chaotic, unpredictable performance brings a terrifying realism...
        </p>
      </div>

      <div class="related-movie">
        <h3>Related Movie</h3>
        <div class="movie-card">
          <div class="movie-thumbnail"></div>
          <div class="movie-info">
            <strong>The Dark Knight</strong><br>
            Action, Crime, Drama · 2008 · 152 min<br>
            <span class="rating">★ 9.0/10</span>
          </div>
        </div>
      </div>

      <div class="comments">
        <h3>Comments (23)</h3>
        <div class="comment-box">
          <div class="comment-header">
            <div class="comment-author">
              <img src="https://i.pravatar.cc/30?img=5" alt="Sarah">
              <span>Sarah Wilson</span>
            </div>
            <span class="comment-meta">2 hours ago</span>
          </div>
          <div class="comment-text">
            Absolutely agree! Heath Ledger’s performance was phenomenal.
          </div>
          <div class="comment-actions">
            <button>Reply</button>
            <button>Edit</button>
          </div>
        </div>

        <div class="comment-box">
          <div class="comment-header">
            <div class="comment-author">
              <img src="https://i.pravatar.cc/30?img=8" alt="Mike">
              <span>Mike Johnson</span>
            </div>
            <span class="comment-meta">5 hours ago</span>
          </div>
          <div class="comment-text">
            Great analysis! This film deserves credit for its practical effects and cinematography.
          </div>
          <div class="comment-actions">
            <button>Reply</button>
          </div>
        </div>
      </div>
    </div>

    <!-- 사이드바 -->
    <div class="sidebar">
      <div class="sidebar-section">
        <h3>Author's Posting Activity</h3>
        <img src="https://dummyimage.com/300x150/2a2a2a/ffffff&text=Activity+Chart" width="100%">
      </div>

      <div class="sidebar-section">
        <h3>Related Posts</h3>
        <div class="related-post">
          <a href="#">Inception: Dreams Within Dreams</a><br>
          <small>by John Doe</small>
        </div>
        <div class="related-post">
          <a href="#">Nolan’s Filmography Ranked</a><br>
          <small>by John Doe</small>
        </div>
        <div class="related-post">
          <a href="#">Batman Begins vs Dark Knight</a><br>
          <small>by John Doe</small>
        </div>
      </div>
    </div>
  </div>
</body>
</html>
