<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${post.title} - 리뷰 상세보기</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_API_KEY&libraries=services"></script>
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp" />

<div class="page-wrapper">
  <div class="detail-container">
    <h2>${post.title}</h2>

    <!-- 🧩 작성자 / 날짜 -->
    <p class="meta">
      작성자:
      <c:choose>
        <c:when test="${post.userId == 101}">하영</c:when>
        <c:when test="${post.userId == 102}">지훈</c:when>
        <c:when test="${post.userId == 103}">유나</c:when>
        <c:when test="${post.userId == 104}">민석</c:when>
        <c:otherwise>익명</c:otherwise>
      </c:choose>
      | 관람일: ${post.viewTime}
    </p>

    <!-- ⭐ 평점 -->
    <div class="rating-box">
      🎬 상영관 별점:
      <c:forEach begin="1" end="5" var="i">
        <c:choose><c:when test="${i <= post.cinemaRating}">★</c:when><c:otherwise>☆</c:otherwise></c:choose>
      </c:forEach>
      &nbsp;|&nbsp;
      💺 좌석 별점:
      <c:forEach begin="1" end="5" var="i">
        <c:choose><c:when test="${i <= post.seatRating}">★</c:when><c:otherwise>☆</c:otherwise></c:choose>
      </c:forEach>
    </div>

    <!-- 🎬 상영관 / 브랜드 / 지역 -->
    <p>
      상영관:
      <c:choose>
        <c:when test="${fn:contains(post.title, 'CGV')}">CGV</c:when>
        <c:when test="${fn:contains(post.title, '메가박스')}">메가박스</c:when>
        <c:when test="${fn:contains(post.title, '롯데시네마')}">롯데시네마</c:when>
        <c:otherwise>기타</c:otherwise>
      </c:choose>
    </p>
    <p>관람시간: ${post.viewTime}</p>
    <p>총평: ${post.overallReview}</p>

    <hr>
    <div class="content-box">
      <p>${post.overallReview}</p>
    </div>

    <!-- 🗺 지도 -->
    <div id="map"></div>

    <!-- 🔘 버튼 -->
    <div style="margin-top:30px; display:flex; gap:10px;">
      <a href="${pageContext.request.contextPath}/list.cinema" class="btn-secondary">← 목록</a>
      <a href="${pageContext.request.contextPath}/edit.cinema?id=${post.id}" class="btn-primary">수정</a>
      <a href="${pageContext.request.contextPath}/delete.cinema?id=${post.id}" class="btn-danger"
         onclick="return confirm('삭제하시겠습니까?');">삭제</a>
    </div>
  </div>
</div>

<!-- ✅ Kakao 지도: 더미 좌표 처리 -->
<script>
  const mapContainer = document.getElementById('map');
  const defaultLat = 37.5665;
  const defaultLng = 126.9780;
  const map = new kakao.maps.Map(mapContainer, {
    center: new kakao.maps.LatLng(defaultLat, defaultLng),
    level: 3
  });
  const geocoder = new kakao.maps.services.Geocoder();
  const address = "${post.cinemaAddress}";

  if (address && address.trim() !== "") {
    geocoder.addressSearch(address, function(result, status) {
      if (status === kakao.maps.services.Status.OK) {
        const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
        new kakao.maps.Marker({ map: map, position: coords });
        map.setCenter(coords);
      } else {
        new kakao.maps.Marker({ map: map, position: map.getCenter() });
      }
    });
  } else {
    new kakao.maps.Marker({ map: map, position: map.getCenter() });
  }
</script>
</body>
</html>
