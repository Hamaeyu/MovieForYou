<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>자유게시판</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/freeDetail.css">
</head>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
<body>
    <div class="forum-post-detail">
        <div class="container">
            <div class="post-card">
                <div class="post-header">
                    <div class="post-number">글번호 : ${freeDetail.id}</div>
                    <h1 class="post-title">${freeDetail.postTitle}</h1>
                    <div class="post-meta">
                        <div class="author">
                            <div class="author-icon">${oneNickname}</div>
                            <span class="author-name">${freeDetail.nickname}</span>
                        </div>
                        <span class="divider">|</span>
                        <div class="timestamp">
                            <svg fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                    clip-rule="evenodd" />
                            </svg>
                            <span>${formattedCreatedAt}</span>
                        </div>
                        <c:if test="${not empty formattedUpdatedAt}">
                            <span class="edited-label">${formattedUpdatedAt}</span>
                        </c:if>
                    </div>
                </div>

                <div class="post-content">
                    ${freeDetail.postContent}
                </div>

                <div class="action-buttons">
                    <a href="/list.free" class="btn btn-primary">목록으로</a>
                    <button class="btn btn-secondary">수정</button>
                    <button class="btn btn-delete">삭제</button>
                </div>
                <!-- 댓글 섹션 -->
                <!-- 로그인 상태일 때만 보이게 설정 -->
                <c:if test="${not empty sessionScope.loginUser}">
                    <div class="comments-section">
                        <div class="comments-header">
                            <h2 class="comments-title">
                                댓글 <span class="comments-count">0</span>
                            </h2>
                        </div>

                        <!-- 댓글 작성 폼 -->
                        <div class="comment-write-box">
                            <div class="comment-author-info">
                                <div class="comment-author-icon">${loginUserNicknamefirst}</div>
                                <span class="comment-author-name">${loginUserNickname}</span>
                            </div>
                            <textarea class="comment-textarea"
                                placeholder="댓글을 입력하세요..."></textarea>
                            <div class="comment-actions">
                                <button class="btn-comment-submit">댓글
                                    작성</button>
                            </div>
                        </div>

                        <!-- 댓글 목록 -->
                        <div class="comments-list">
                            <!-- 댓글이 여기에 동적으로 추가됩니다 -->

                        </div>
                    </div>
                </c:if>
                <!-- 댓글 끝 -->
            </div>
        </div>
    </div>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</html>