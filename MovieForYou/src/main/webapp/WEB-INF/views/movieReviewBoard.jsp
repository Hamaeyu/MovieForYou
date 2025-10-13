<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/header.jsp" />

<main class="review-container">
    <section class="community-section">
        <h2>Community Reviews</h2>
        <p>Share your thoughts and discover what others think</p>

        <!-- 카테고리 탭 -->
        <div class="tab-menu">
            <button class="tab active">Movie Reviews</button>
            <button class="tab">Notices</button>
            <button class="tab">Movie Ratings</button>
            <button class="tab">Event Reviews</button>
        </div>

        <!-- 필터 -->
        <div class="filter-bar">
            <select class="filter-select">
                <option>Latest</option>
                <option>Most Viewed</option>
                <option>Most Commented</option>
            </select>
            <select class="filter-select">
                <option>All Categories</option>
                <option>Action</option>
                <option>Drama</option>
                <option>Thriller</option>
            </select>
        </div>

        <!-- 게시글 리스트 -->
        <div class="review-list">
            <c:forEach var="review" items="${reviewList}">
                <div class="review-card">
                    <div class="review-thumb">
                        <i class="fa-solid fa-film"></i>
                    </div>
                    <div class="review-content">
                        <h3>${review.title}</h3>
                        <p>${review.summary}</p>
                        <span class="views">${review.views} views</span>
                    </div>
                    <div class="review-meta">
                        <span class="author">${review.author}</span>
                        <span class="date">${review.date}</span>
                        <span class="comment"><i class="fa-regular fa-comment"></i> ${review.commentCount}</span>
                        <span class="like"><i class="fa-regular fa-heart"></i> ${review.likes}</span>
                    </div>
                </div>
            </c:forEach>

            <!-- 예시용 static 데이터 -->
            <div class="review-card">
                <div class="review-thumb"><i class="fa-solid fa-film"></i></div>
                <div class="review-content">
                    <h3>The Dark Knight: A Masterpiece of Modern Cinema</h3>
                    <p>Christopher Nolan’s masterpiece continues to influence superhero films today...</p>
                    <span class="views">1,234 views</span>
                </div>
                <div class="review-meta">
                    <span class="author">John Doe</span>
                    <span class="date">2 days ago</span>
                    <span class="comment"><i class="fa-regular fa-comment"></i> 23</span>
                    <span class="like"><i class="fa-regular fa-heart"></i> 156</span>
                </div>
            </div>

            <div class="review-card">
                <div class="review-thumb"><i class="fa-solid fa-film"></i></div>
                <div class="review-content">
                    <h3>Inception: Dreams Within Dreams - A Deep Analysis</h3>
                    <p>Diving deep into the layered narrative of Inception, exploring Nolan’s complex storylines...</p>
                    <span class="views">2,890 views</span>
                </div>
                <div class="review-meta">
                    <span class="author">Sarah Wilson</span>
                    <span class="date">1 week ago</span>
                    <span class="comment"><i class="fa-regular fa-comment"></i> 67</span>
                    <span class="like"><i class="fa-regular fa-heart"></i> 289</span>
                </div>
            </div>

            <div class="review-card">
                <div class="review-thumb"><i class="fa-solid fa-film"></i></div>
                <div class="review-content">
                    <h3>Parasite: Social Commentary Through Brilliant Filmmaking</h3>
                    <p>Bong Joon-ho’s masterful exploration of class divide through the lens of two families...</p>
                    <span class="views">1,567 views</span>
                </div>
                <div class="review-meta">
                    <span class="author">Mike Johnson</span>
                    <span class="date">3 days ago</span>
                    <span class="comment"><i class="fa-regular fa-comment"></i> 34</span>
                    <span class="like"><i class="fa-regular fa-heart"></i> 198</span>
                </div>
            </div>
        </div>

        <!-- 페이지네이션 -->
        <div class="pagination">
            <button class="prev">‹ Previous</button>
            <button class="page active">1</button>
            <button class="page">2</button>
            <button class="page">3</button>
            <button class="next">Next ›</button>
        </div>
    </section>
</main>

<style>
body {
    background-color: #121212;
    color: #f1f1f1;
    font-family: "Inter", sans-serif;
}
.review-container {
    max-width: 900px;
    margin: 50px auto;
    padding: 0 20px;
}
.community-section h2 {
    font-size: 24px;
    margin-bottom: 5px;
}
.community-section p {
    color: #aaa;
    margin-bottom: 20px;
}
.tab-menu {
    display: flex;
    gap: 10px;
    margin-bottom: 20px;
}
.tab {
    background: #2a2a2a;
    color: #fff;
    border: none;
    border-radius: 6px;
    padding: 8px 15px;
    cursor: pointer;
}
.tab.active {
    background: #ffd43b;
    color: #000;
    font-weight: 600;
}
.filter-bar {
    display: flex;
    justify-content: flex-start;
    gap: 10px;
    margin-bottom: 20px;
}
.filter-select {
    background: #2a2a2a;
    border: none;
    color: #fff;
    padding: 6px 12px;
    border-radius: 5px;
}
.review-list {
    display: flex;
    flex-direction: column;
    gap: 15px;
}
.review-card {
    background: #1e1e1e;
    border-radius: 10px;
    padding: 20px;
    display: flex;
    align-items: flex-start;
    justify-content: space-between;
    transition: background 0.2s ease;
}
.review-card:hover {
    background: #252525;
}
.review-thumb {
    font-size: 28px;
    color: #ccc;
    margin-right: 15px;
}
.review-content {
    flex: 1;
}
.review-content h3 {
    font-size: 16px;
    margin-bottom: 5px;
    color: #fff;
}
.review-content p {
    font-size: 14px;
    color: #aaa;
    margin-bottom: 5px;
}
.views {
    font-size: 13px;
    color: #888;
}
.review-meta {
    font-size: 13px;
    color: #aaa;
    display: flex;
    flex-direction: column;
    align-items: flex-end;
    gap: 3px;
}
.review-meta span i {
    margin-right: 3px;
}
.pagination {
    display: flex;
    justify-content: center;
    gap: 5px;
    margin-top: 25px;
}
.pagination button {
    background: #2a2a2a;
    color: #fff;
    border: none;
    border-radius: 6px;
    padding: 6px 12px;
    cursor: pointer;
}
.pagination .page.active {
    background: #ffd43b;
    color: #000;
    font-weight: 600;
}
.pagination .next {
    background: #00a25a;
}
</style>
>