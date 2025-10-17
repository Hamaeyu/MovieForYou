<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${post.title} - 리뷰 상세보기</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="https://dapi.kakao.com/v2/maps/sdk.js?appkey=YOUR_KAKAO_API_KEY&libraries=services"></script>
</head>
<body>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp" />

<div class="detail-container">
  <h2>${post.title}</h2>
  <p class="meta">작성자: ${post.writer} | 작성일: ${post.createdAt}</p>

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

  <p>상영관: ${post.cinemaName} (${post.brandName}, ${post.regionName})</p>
  <p>주소: ${post.cinemaAddress}</p>
  <p>관람시간: ${post.viewTime}</p>
  <p>총평: ${post.overallReview}</p>

  <hr>
  <div class="content-box">${post.content}</div>
  <div id="map"></div>

  <div class="btn-group">
    <a href="${pageContext.request.contextPath}/list.cinema" class="btn-secondary">목록</a>
    <c:if test="${sessionScope.loginUserId != null}">
      <a href="${pageContext.request.contextPath}/edit.cinema?id=${post.id}" class="btn-primary">수정</a>
      <form action="${pageContext.request.contextPath}/delete.cinema" method="post" style="display:inline;">
        <input type="hidden" name="id" value="${post.id}">
        <button type="submit" class="btn-danger">삭제</button>
      </form>
    </c:if>
  </div>

  <h3>평균 통계</h3>
  <canvas id="chart"></canvas>
</div>

<script>
  // Kakao Map
  const mapContainer = document.getElementById('map');
  const map = new kakao.maps.Map(mapContainer, {center: new kakao.maps.LatLng(37.5665,126.9780), level:3});
  const geocoder = new kakao.maps.services.Geocoder();
  geocoder.addressSearch("${post.cinemaAddress}", function(result, status){
    if(status === kakao.maps.services.Status.OK){
      const coords = new kakao.maps.LatLng(result[0].y, result[0].x);
      new kakao.maps.Marker({map: map, position: coords});
      map.setCenter(coords);
    }
  });

  // Chart.js
  fetch(`${pageContext.request.contextPath}/stats.cinema?cinemaId=${post.cinemaId}`)
    .then(res=>res.json())
    .then(d=>{
      new Chart(document.getElementById('chart'), {
        type:'bar',
        data:{
          labels:['상영관','좌석'],
          datasets:[{data:[d.avgCinema, d.avgSeat], backgroundColor:['#4caf50','#2196f3']}]
        },
        options:{scales:{y:{beginAtZero:true, max:5}}}
      });
    });
</script>
</body>
</html>
