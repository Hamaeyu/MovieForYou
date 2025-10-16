<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/freeDetail.css">
</head>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
<body>
    <div class="forum-post-detail">
        <div class="container">
            <div class="post-card">
                <div class="post-header">
                    <div class="post-number">#12345</div>
                    <h1 class="post-title">주말에 봤던 영화 추천합니다!</h1>
                    <div class="post-meta">
                        <div class="author">
                            <div class="author-icon">영</div>
                            <span class="author-name">영화마니아</span>
                        </div>
                        <span class="divider">|</span>
                        <div class="timestamp">
                            <svg fill="currentColor" viewBox="0 0 20 20">
                                <path fill-rule="evenodd"
                                    d="M10 18a8 8 0 100-16 8 8 0 000 16zm1-12a1 1 0 10-2 0v4a1 1 0 00.293.707l2.828 2.829a1 1 0 101.415-1.415L11 9.586V6z"
                                    clip-rule="evenodd" />
                            </svg>
                            <span>2024년 10월 17일 14:32</span>
                        </div>
                        <span class="edited-label">(수정됨: 2024년
                            10월 17일 15:20)</span>
                    </div>
                </div>

                <div class="post-content">안녕하세요! 주말에 다크 나이트를 다시
                    봤는데 정말 명작이네요. 크리스토퍼 놀란 감독의 연출력이 돋보이는 작품으로, 히스 레저의 조커
                    연기는 정말 압도적이었습니다. 영화를 보면서 선과 악의 경계에 대해 다시 한번 생각해보게
                    되었어요. 특히 페리 장면들의 긴장감은 지금 봐도 손에 땀을 쥐게 만드네요. 아직 안 보신
                    분들은 꼭 한번 보시길 추천드립니다! 여러분은 어떤 영화를 최근에 보셨나요? 추천해주세요!</div>

                <div class="action-buttons">
                    <a href="/list.free" class="btn btn-primary">목록으로</a>
                    <button class="btn btn-secondary">수정</button>
                    <button class="btn btn-delete">삭제</button>
                </div>
            </div>
        </div>
    </div>
</body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</html>