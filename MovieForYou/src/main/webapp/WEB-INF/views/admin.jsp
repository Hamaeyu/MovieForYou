<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
  	<meta charset="UTF-8">
  	<title>Admin Dashboard</title>
	<link 
	 href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" 
	 rel="stylesheet" 
	 integrity="sha384-QWTKZyjpPEjISv5WaRU9OFeRpok6YctnYmDr5pNlyT2bRjXh0JMhjY6hW+ALEwIH" 
	 crossorigin="anonymous">
  <style>
    body {
      background-color: #111;
      color: #fff !important;
      font-family: 'Inter', sans-serif;
    }

    h1 { font-size: 1.5rem; margin-bottom: 20px; }

    .dashboard {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
      gap: 20px;
      margin-bottom: 30px;
      padding: 30px;
    }

    .card { background-color: #1c1c1c; border-radius: 12px; padding: 20px; box-shadow: 0 0 4px rgba(0,0,0,0.2); }
    .metric { display: flex; align-items: center; justify-content: space-between; color: #fff !important; }
    .metric-icon { background-color: #222; border-radius: 50%; padding: 10px; font-size: 1.2rem; }
    .card h2 { font-size: 2rem; margin-bottom: 5px; }
    .card small { color: #999; }

    .green { color: #00e676; } .yellow { color: #ffd43b; } .orange { color: #ff9800; } .pink { color: #ff4081; }

    .section { display: flex; flex-wrap: wrap; gap: 20px; }
    .user-management, .popular { flex: 1 1 45%; background-color: #1c1c1c; border-radius: 12px; padding: 20px; }

    .user-header { display: flex; align-items: center; justify-content: space-between; margin-bottom: 15px; }
    .user-header select, .user-header button { background-color: #2a2a2a; border: none; color: #fff; padding: 8px 12px; border-radius: 8px; cursor: pointer; }

    .user-list { display: flex; flex-direction: column; gap: 12px; height: 350px; overflow-y: scroll; }
    .user { display: flex; align-items: center; justify-content: space-between; background-color: #2a2a2a; padding: 10px 14px; border-radius: 8px; }
    .user-info { display: flex; align-items: center; gap: 10px; }
    .user-avatar { width: 36px; height: 36px; border-radius: 50%; display: flex; align-items: center; justify-content: center; color: #fff; font-weight: bold; }
    .user-avatar.green { background-color: #00e676; } .user-avatar.orange { background-color: #ff9800; } .user-avatar.pink { background-color: #ff4081; }
    .user-email { color: #aaa; font-size: 0.9rem; }

    .popular h3 { margin-bottom: 10px; }
    .chart { background-color: #2a2a2a; border-radius: 8px; height: 320px; margin-bottom: 15px; display: flex; align-items: flex-end; justify-content: space-evenly; padding: 10px; }
    .bar { width: 20px; border-radius: 4px 4px 0 0; }
    .bar.green { background-color: #00e676; height: 60px; }
    .bar.yellow { background-color: #ffd43b; height: 90px; }
    .bar.orange { background-color: #ff9800; height: 80px; }
    .bar.blue { background-color: #2196f3; height: 70px; }

    .movie-list { list-style: none; }
    .movie-list li { display: flex; justify-content: space-between; color: #ccc; padding: 4px 0; }
    .movie-list span { color: #00e676; }

    /* sentinel 가시성(선택) */
    #sentinel { color: #bbb; font-size: 0.9rem; padding: 8px 0; text-align: center; }
    @media (max-width: 768px) { .section { flex-direction: column; } }
  </style>
</head>
<body>
  <jsp:include page="/WEB-INF/views/header.jsp"/>
  <h1>Admin Dashboard</h1>

  <div class="dashboard">
    <div class="card">
      <div class="metric">
        <h2 class="freeboard"></h2>
        <div class="metric-icon green">👥</div>
      </div>
      <small>자유게시판</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2 class="reviewboard"></h2>
        <div class="metric-icon yellow">📝</div>
      </div>
      <small>영화 후기 게시판</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2 class="cinemaboard"></h2>
        <div class="metric-icon orange">📄</div>
      </div>
      <small>영화관 후기 게시판</small>
    </div>

    <div class="card">
      <div class="metric">
        <h2 class="total"></h2>
        <div class="metric-icon pink">⭐</div>
      </div>
      <small>Total</small>
    </div>
  </div>

  <div class="section">
    <div class="user-management">
      <div class="user-header">
        <h3>영화관 관리</h3>
        <div>
          <button class="btn btn-warning" data-bs-toggle="modal" data-bs-target="#theaterModal">영화관 추가</button>
        </div>
      </div>

      <div id="theater-container" class="user-list">
        <%@ include file="/WEB-INF/views/cinemaList.jsp" %>
        <div id="sentinel">Loading...</div>
      </div>
    </div>
  	<%@ include file="/WEB-INF/views/adminChart.jsp" %>

  </div>

  <%@ include file="/WEB-INF/views/adminModal.jsp" %>
  <%@ include file="/WEB-INF/views/editTheaterModal.jsp" %>

  <script 
    src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js" 
    integrity="sha384-YvpcrYf0tY3lHB60NNkmXc5s9fDVZLESaAA55NDzOxhy9GkcIdslK1eN7N6jIeHz" 
    crossorigin="anonymous"></script>

  <script src="https://code.highcharts.com/highcharts.src.js"></script>
  <script src="https://code.highcharts.com/modules/funnel.js"></script>
  <script>
	let currentPage = 1;
	let isLoading = false;
	let observer;
	
	function loadMore() {
		  if (isLoading) return;
		  isLoading = true;
		  currentPage++;
	
		  fetch('<%= request.getContextPath() %>/admin/more?page=' + currentPage)
		    .then(res => res.text())
		    .then(html => {
		      if (html.trim().length === 0) {
		        // ✅ 서버가 빈 응답을 보냈을 때 옵저버 종료
		        if (observer) observer.disconnect();
		        const sentinel = document.querySelector("#sentinel");
		        if (sentinel) {
		          sentinel.textContent = "모든 데이터를 불러왔습니다.";
		        }
		        return;
		      }
	
		      document.querySelector("#theater-container").insertAdjacentHTML('beforeend', html);
		      isLoading = false;
	
		      const container = document.querySelector('#theater-container');
		      const sentinel = document.querySelector('#sentinel');
		      container.appendChild(sentinel);
		    })
		    .catch(err => {
		      console.error(err);
		      isLoading = false;
		    });
		}
	
	
	function initObserver() {
	  const container = document.querySelector('#theater-container');
	  const sentinel = document.querySelector('#sentinel');
	  
	  if (!container || !sentinel) {
		    console.warn("observer init 실패: 요소 없음");
		    return;
	  }
	
	  observer = new IntersectionObserver((entries) => {
	    const entry = entries[0];
	    if (entry.isIntersecting && !isLoading) {
	      loadMore();
	    }
	  }, {
	    root: container,
	    rootMargin: '0px 0px 0px 0px',
	    threshold: 0.1
	  });
	
	  observer.observe(sentinel);
	
	  if (container.scrollHeight <= container.clientHeight) {
	    loadMore();
	  }
	}
	
	document.addEventListener("DOMContentLoaded", initObserver);
	
	function deleteCinema(id) {
		  if (confirm("정말 삭제하시겠습니까?")) {
			fetch("${pageContext.request.contextPath}/admin/deleteCinema",  {
		      method: "POST",
		      headers: { "Content-Type": "application/x-www-form-urlencoded" },
		      body: "id=" + id
		    }).then(res => {
		      if (res.ok) {
		        alert("삭제 완료!");
		        location.reload();
		      } else {
		        alert("삭제 실패");
		      }
		    });
		  }
		}
	document.addEventListener("DOMContentLoaded", function() {
		const BASE = '${pageContext.request.contextPath}';
	    fetch(`${BASE}/admin/chartData`)
	    .then(res => res.json())
	    .then(data => {
	        const free = data.free ?? 0;
	        const movie = data.movieReview ?? 0;
	        const cinema = data.cinemaReview ?? 0;

	        // 자유게시판
	        const freeboardEl = document.querySelector('.freeboard');
	        if (freeboardEl) freeboardEl.textContent = free;

	        // (참고: 다른 카드도 이런 식으로)
	        const movieEl = document.querySelector('.reviewboard');
	        if (movieEl) movieEl.textContent = movie;

	        const cinemaEl = document.querySelector('.cinemaboard');
	        if (cinemaEl) cinemaEl.textContent = cinema;
	        
	        const totalEl = document.querySelector('.total');
	        if (totalEl) totalEl.textContent = free+movie+cinema;
	    })
	    .catch(err => {
	    	console.error('대시보드 데이터 불러오기 실패:', err);
	    })
	});
	
</script>
</body>
</html>
