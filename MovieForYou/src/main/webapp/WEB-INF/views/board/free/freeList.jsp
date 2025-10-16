<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유게시판</title>
<link rel="stylesheet" type="text/css"
    href="${pageContext.request.contextPath}/css/freeList.css">
</head>

<jsp:include
    page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>

<body class="body-free-list">
    <div class="container-list">
        <div class="container-list-inner">
            <h1 class="section-title">자유게시판</h1>

            <div class="filters">
                <button class="filter-btn">최신순</button>
                <button class="filter-btn">등록순</button>
            </div>

            <!-- 서버 데이터로 렌더링할 영역 -->
            <div class="posts-grid">
                <c:if test="${not empty freeList}">
                    <c:forEach var="post" items="${freeList}">
                        <c:if test="${not empty post}">
                            <div class="post-card" data-id="${post.id}">
                                <div class="post-thumbnail">
                                    <img src="${post.imageUrl}"
                                        alt="게시글 이미지" />
                                </div>
                                <div class="post-info">
                                    <div class="post-title">
                                        ${post.postTitle}</div>
                                    <div class="post-meta">
                                        <div class="post-author">
                                            <div class="author-avatar">${post.oneNickname}</div>
                                            <span>${post.shortNickname}</span>
                                        </div>
                                        <div class="post-stats">
                                            <span class="stat-item">${post.createdAtStr}</span>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:if>
                    </c:forEach>
                </c:if>
            </div>
            </div>
            <button class="load-more" id="loadMoreBtn">더 보기</button>
        </div>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/freeList.js"></script>
</html>