<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>상영관 평가 작성</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="page-wrapper">
  <div class="detail-container">
    <h2>🏢 상영관 평가 작성</h2>
    <p class="meta">영화관 이용 경험을 자유롭게 평가해주세요.</p>

    <form id="reviewForm">
      <!-- 제목 -->
      <div class="form-group">
        <label>제목</label>
        <input type="text" name="title" required
               placeholder="예: CGV 용산아이파크몰 - 사운드 최고!" style="width:100%; padding:10px; border-radius:6px; border:1px solid #444; background:#222; color:#fff;">
      </div>

      <!-- 브랜드 / 지역 / 상영관 -->
      <div class="form-group">
        <label>브랜드</label>
        <select name="brand" required style="width:100%; padding:10px; border-radius:6px; background:#222; color:#fff; border:1px solid #444;">
          <option value="">선택하세요</option>
          <option value="CGV">CGV</option>
          <option value="메가박스">메가박스</option>
          <option value="롯데시네마">롯데시네마</option>
        </select>
      </div>

      <div class="form-group">
        <label>지역</label>
        <select name="region" required style="width:100%; padding:10px; border-radius:6px; background:#222; color:#fff; border:1px solid #444;">
          <option value="">선택하세요</option>
          <option value="서울">서울</option>
          <option value="경기">경기</option>
          <option value="인천">인천</option>
          <option value="부산">부산</option>
          <option value="기타">기타</option>
        </select>
      </div>

      <div class="form-group">
        <label>상영관 이름</label>
        <input type="text" name="cinemaName" required placeholder="예: CGV 용산아이파크몰"
               style="width:100%; padding:10px; border-radius:6px; border:1px solid #444; background:#222; color:#fff;">
      </div>

      <!-- 별점 -->
      <div class="form-group">
        <label>상영관 별점</label>
        <select name="cinemaRating" required style="width:100%; padding:10px; border-radius:6px; background:#222; color:#fff; border:1px solid #444;">
          <option value="">선택하세요</option>
          <option value="5">★★★★★</option>
          <option value="4">★★★★☆</option>
          <option value="3">★★★☆☆</option>
          <option value="2">★★☆☆☆</option>
          <option value="1">★☆☆☆☆</option>
        </select>
      </div>

      <div class="form-group">
        <label>좌석 별점</label>
        <select name="seatRating" required style="width:100%; padding:10px; border-radius:6px; background:#222; color:#fff; border:1px solid #444;">
          <option value="">선택하세요</option>
          <option value="5">★★★★★</option>
          <option value="4">★★★★☆</option>
          <option value="3">★★★☆☆</option>
          <option value="2">★★☆☆☆</option>
          <option value="1">★☆☆☆☆</option>
        </select>
      </div>

      <!-- 관람일 -->
      <div class="form-group">
        <label>관람일</label>
        <input type="date" name="viewTime"
               style="width:100%; padding:10px; border-radius:6px; border:1px solid #444; background:#222; color:#fff;">
      </div>

      <!-- 총평 -->
      <div class="form-group">
        <label>총평</label>
        <textarea name="overallReview" rows="4" placeholder="예: IMAX 사운드가 압도적이었어요. 직원분도 친절했습니다."
                  style="width:100%; padding:10px; border-radius:6px; border:1px solid #444; background:#222; color:#fff;"></textarea>
      </div>

      <!-- 버튼 -->
      <div style="margin-top:30px; display:flex; gap:10px;">
        <button type="button" class="btn-primary" onclick="submitDummy()">작성 완료</button>
        <a href="${pageContext.request.contextPath}/list.cinema" class="btn-secondary">← 목록으로</a>
      </div>
    </form>
  </div>
</div>

<script>
  function submitDummy() {
    alert("작성이 완료되었습니다.");
    window.location.href = "${pageContext.request.contextPath}/list.cinema";
  }
</script>
</body>
</html>
