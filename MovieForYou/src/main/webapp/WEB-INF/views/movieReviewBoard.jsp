<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<jsp:include page="/WEB-INF/views/header.jsp" />
<main class="review-container">
    <section class="community-section">
        <h2>Community Reviews</h2>
        <p>Share your thoughts and discover what others think</p>

        <!-- 카테고리 탭 -->
        <div class="tab-menu">
            <button class="tab active" onclick="setActive(this)">영화 리뷰</button>
            <button class="tab" onclick="setActive(this)">공지사항</button>
            <button class="tab" onclick="setActive(this)">영화관 리뷰</button>
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
            <button class="write-button" onclick="navigateToWritePage()">write</button>
        </div>

        <!-- 게시글 리스트 -->
        <div class="review-list">
		  <c:forEach var="review" items="${reviewList}">
		    <div class="review-card">
		      <div class="review-thumb"><i class="fa-solid fa-film"></i></div>
		      <div class="review-content">
		        <h3><a href="movieReviewDetail?id=${review.id}">${review.title}</a></h3>
		        <p>${review.review}</p>
		      </div>
		      <div class="review-meta">
		        <span class="author">${review.userId}</span>
		        <span class="date">
		          <fmt:formatDate value="${review.createdAt}" pattern="yyyy-MM-dd HH:mm" />
		        </span>
		      </div>
		    </div>
		  </c:forEach>
		</div>


        <!-- 페이지네이션 -->
        <div class="pagination">
		  <c:if test="${currentPage > 1}">
		    <a href="movieReview?page=${currentPage - 1}" class="page-btn">이전</a>
		  </c:if>
		
		  <c:forEach var="i" begin="1" end="${totalPage}">
		    <a href="movieReview?page=${i}" class="page-num
		       ${i == currentPage ? 'active' : ''}">${i}</a>
		  </c:forEach>
		
		  <c:if test="${currentPage < totalPage}">
		    <a href="movieReview?page=${currentPage + 1}" class="page-btn">다음</a>
		  </c:if>
		</div>
    </section>
</main>
<script>
function setActive(element) {
    document.querySelectorAll('.tab').forEach(item => {
        item.classList.remove('active');
    });
    element.classList.add('active');
}
function navigateToWritePage() {
	location.href = "/reviewBoardWrite";
}
</script>
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
.tab, .write-button {
    background: #2a2a2a;
    color: #fff;
    border: none;
    border-radius: 6px;
    padding: 8px 15px;
    cursor: pointer;
}
.tab.active, .write-button {
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
    overflow-y: scroll;
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
  text-align: center;
  margin-top: 40px;
}
.page-btn, .page-num {
  display: inline-block;
  margin: 0 5px;
  padding: 6px 12px;
  color: #fff;
  background: #333;
  border-radius: 4px;
  text-decoration: none;
}
.page-num.active {
  background: #ffd43b;
  color: #fff;
  font-weight: bold;
}
.page-btn:hover, .page-num:hover {
  background: #555;
}
</style>
>