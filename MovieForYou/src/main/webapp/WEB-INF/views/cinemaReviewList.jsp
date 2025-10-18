<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>🎬 상영관 리뷰 게시판</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="page-wrapper">
  <div class="list-container">
    <!-- ✅ 상단 제목 / 글쓰기 버튼 -->
    <div class="list-header">
      <h2>🎬 상영관 리뷰 게시판</h2>
      <a href="${pageContext.request.contextPath}/write.cinema" class="btn-primary">+ 글쓰기</a>
    </div>

    <!-- 🔍 검색 -->
    <div class="search-bar">
      <form method="get" action="${pageContext.request.contextPath}/list.cinema">
        <select name="field">
          <option value="title" ${field eq 'title' ? 'selected' : ''}>제목</option>
          <option value="writer" ${field eq 'writer' ? 'selected' : ''}>작성자</option>
        </select>
        <input type="text" name="q" value="${q}" placeholder="검색어를 입력하세요">
        <button type="submit">검색</button>
      </form>
    </div>

    <!-- 📋 리뷰 목록 -->
    <table class="review-table">
      <thead>
        <tr>
          <th>번호</th>
          <th>제목</th>
          <th>작성자</th>
          <th>상영관</th>
          <th>좌석/관 평점</th>
          <th>관람일</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${empty list}">
            <tr><td colspan="6">등록된 리뷰가 없습니다.</td></tr>
          </c:when>
          <c:otherwise>
            <c:forEach var="p" items="${list}" varStatus="i">
              <tr>
                <td>${page.offset + i.index + 1}</td>
                <td>
                  <a href="${pageContext.request.contextPath}/detail.cinema?id=${p.id}" style="color:#4caf50; text-decoration:none;">
                    ${p.title}
                  </a>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${p.userId == 101}">유정</c:when>
                    <c:when test="${p.userId == 102}">마리</c:when>
                    <c:when test="${p.userId == 103}">하영</c:when>
                    <c:when test="${p.userId == 104}">일반회원</c:when>
                    <c:otherwise>익명</c:otherwise>
                  </c:choose>
                </td>
                <td>
                  <c:choose>
                    <c:when test="${fn:contains(p.title, 'CGV')}">CGV</c:when>
                    <c:when test="${fn:contains(p.title, '메가박스')}">메가박스</c:when>
                    <c:when test="${fn:contains(p.title, '롯데시네마')}">롯데시네마</c:when>
                    <c:otherwise>기타</c:otherwise>
                  </c:choose>
                </td>
                <td>
                  좌석:
                  <c:forEach begin="1" end="5" var="s">
                    <c:choose>
                      <c:when test="${s <= p.seatRating}">★</c:when>
                      <c:otherwise>☆</c:otherwise>
                    </c:choose>
                  </c:forEach>
                  <br>
                  관:
                  <c:forEach begin="1" end="5" var="s">
                    <c:choose>
                      <c:when test="${s <= p.cinemaRating}">★</c:when>
                      <c:otherwise>☆</c:otherwise>
                    </c:choose>
                  </c:forEach>
                </td>
                <td>${p.viewTime}</td>
              </tr>
            </c:forEach>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>

    <!-- 📄 페이지네이션 -->
    <div class="pagination">
      <c:if test="${page.page > 1}">
        <a href="${pageContext.request.contextPath}/list.cinema?page=${page.page-1}&field=${field}&q=${q}">이전</a>
      </c:if>

      <c:forEach var="pno" begin="1" end="${page.pages}">
        <a href="${pageContext.request.contextPath}/list.cinema?page=${pno}&field=${field}&q=${q}"
           class="${pno == page.page ? 'active' : ''}">${pno}</a>
      </c:forEach>

      <c:if test="${page.page < page.pages}">
        <a href="${pageContext.request.contextPath}/list.cinema?page=${page.page+1}&field=${field}&q=${q}">다음</a>
      </c:if>
    </div>
  </div>
</div>
</body>
</html>
