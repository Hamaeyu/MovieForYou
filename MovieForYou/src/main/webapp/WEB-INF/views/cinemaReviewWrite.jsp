<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>영화관 후기 작성</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp" />

<script>window.APP_CTX = '${pageContext.request.contextPath}';</script>

<div class="page-wrapper">
  <div class="cinema-container">
    <h2>🎬 영화관 후기 작성</h2>

    <form method="post" action="${pageContext.request.contextPath}/save.cinema">
      <div class="form-group">
        <label>제목</label>
        <input type="text" name="title" required>
      </div>

      <div class="form-group">
        <label>영화관 선택 (브랜드 → 지역 → 영화관)</label>
        <div class="select-group">
          <select id="brandSel" class="select-input"></select>
          <select id="regionSel" class="select-input" disabled></select>
          <select id="cinemaSel" name="cinema_id" class="select-input" disabled></select>
        </div>
      </div>

      <div class="form-group">
        <label>별점</label>
        <div class="select-group">
          <select name="cinema_rating" class="select-input">
            <c:forEach var="i" begin="1" end="5">
              <option value="${i}">${i}</option>
            </c:forEach>
          </select>
          <select name="seat_rating" class="select-input">
            <c:forEach var="i" begin="1" end="5">
              <option value="${i}">${i}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label>관람 시간</label>
        <input type="text" name="view_time" placeholder="YYYY-MM-DD HH:MM">
      </div>

      <div class="form-group">
        <label>총평</label>
        <input type="text" name="overall_review" maxlength="100" placeholder="간단한 총평을 입력하세요">
      </div>

      <div class="form-group">
        <label>상세 후기 (써머노트)</label>
        <textarea id="summernote" name="content"></textarea>
      </div>

      <button class="submit-btn" type="submit">등록</button>
    </form>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
<script src="${pageContext.request.contextPath}/js/writeCinemaReview.js"></script>
</body>
</html>
