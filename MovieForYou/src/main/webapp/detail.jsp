<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MovieForYou - 영화 상세 정보</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #1a1a2e 0%, #16213e 50%, #0f3460 100%);
            min-height: 100vh;
            color: #fff;
        }
        

        .d-container {
            max-width: 1200px;
            margin: 0 auto;
            margin-top: 50px;
            margin-bottom: 50px;
            background: rgba(0, 0, 0, 0.4);
            backdrop-filter: blur(20px);
            border-radius: 20px;
            padding: 50px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
            border: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .back-btn {
            display: inline-block;
            padding: 12px 30px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a2e;
            text-decoration: none;
            border-radius: 30px;
            margin-bottom: 40px;
            transition: all 0.3s;
            font-weight: bold;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.3);
        }
        
        .back-btn:hover {
            transform: translateY(-3px);
            box-shadow: 0 8px 25px rgba(255, 215, 0, 0.5);
        }
        
        .movie-header {
            text-align: center;
            margin-bottom: 50px;
            padding-bottom: 30px;
            border-bottom: 3px solid rgba(255, 215, 0, 0.5);
        }
        
        .movie-header h1 {
            font-size: 3em;
            color: #ffd700;
            margin-bottom: 15px;
            text-shadow: 0 0 30px rgba(255, 215, 0, 0.5);
        }
        
        .movie-header .subtitle {
            font-size: 1.3em;
            color: rgba(255, 255, 255, 0.7);
        }
        
        .info-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 40px;
        }
        
        .info-card {
            background: linear-gradient(145deg, rgba(255, 215, 0, 0.1) 0%, rgba(255, 215, 0, 0.05) 100%);
            padding: 30px;
            border-radius: 15px;
            box-shadow: 0 8px 20px rgba(0, 0, 0, 0.3);
            border: 1px solid rgba(255, 215, 0, 0.3);
            transition: all 0.3s;
        }
        
        .info-card:hover {
            transform: translateY(-5px);
            box-shadow: 0 12px 30px rgba(255, 215, 0, 0.3);
            border-color: #ffd700;
        }
        
        .info-card h3 {
            color: #ffd700;
            margin-bottom: 20px;
            font-size: 1.3em;
            border-bottom: 2px solid rgba(255, 215, 0, 0.5);
            padding-bottom: 12px;
        }
        
        .info-item {
            margin: 15px 0;
            font-size: 1.05em;
            color: rgba(255, 255, 255, 0.9);
            line-height: 1.6;
        }
        
        .info-item strong {
            color: #ffd700;
            display: inline-block;
            min-width: 120px;
        }
        
        .detail-section {
            background: linear-gradient(145deg, rgba(255, 255, 255, 0.05) 0%, rgba(255, 255, 255, 0.02) 100%);
            padding: 35px;
            border-radius: 15px;
            margin-top: 30px;
            border: 1px solid rgba(255, 215, 0, 0.2);
        }
        
        .detail-section h2 {
            color: #ffd700;
            margin-bottom: 25px;
            font-size: 2em;
            text-shadow: 0 0 20px rgba(255, 215, 0, 0.3);
        }
        
        .detail-content {
            line-height: 1.8;
            color: rgba(255, 255, 255, 0.85);
            font-size: 1.05em;
        }
        
        .loading, .error {
            text-align: center;
            padding: 120px 20px;
            font-size: 1.5em;
        }
        
        .loading {
            color: #ffd700;
            text-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
        }
        
        .error {
            color: #ff6b6b;
        }
        
        .stat-row {
            display: flex;
            justify-content: space-between;
            align-items: center;
            padding: 18px 0;
            border-bottom: 1px solid rgba(255, 255, 255, 0.1);
        }
        
        .stat-row:last-child {
            border-bottom: none;
        }
        
        .stat-label {
            font-weight: bold;
            color: rgba(255, 255, 255, 0.8);
            font-size: 1.1em;
        }
        
        .stat-value {
            font-size: 1.2em;
            color: #ffd700;
            font-weight: bold;
            text-shadow: 0 0 10px rgba(255, 215, 0, 0.3);
        }
    </style>
</head>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
<body>

    <div class="d-container">
        <a href="javascript:history.back()" class="back-btn">← 목록으로 돌아가기</a>
        
        <div id="movieDetail"></div>
    </div>

    <script>
        const API_KEY = 'f19b8399de089df31b5c4de5101998d0';
        const MOVIE_INFO_URL = 'http://kobis.or.kr/kobisopenapi/webservice/rest/movie/searchMovieInfo.json';
        
        // URL 파라미터 가져오기
        function getUrlParameter(name) {
            const urlParams = new URLSearchParams(window.location.search);
            return urlParams.get(name);
        }
        
        // 숫자 포맷팅
        function formatNumber(num) {
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
        
        // 영화 상세 정보 가져오기
        async function fetchMovieDetail() {
            const movieCd = getUrlParameter('movieCd');
            const movieNm = decodeURIComponent(getUrlParameter('movieNm'));
            const container = document.getElementById('movieDetail');
            
            container.innerHTML = '<div class="loading">🎬 영화 상세 정보를 불러오는 중...</div>';
            
            try {
                const response = await fetch(MOVIE_INFO_URL + '?key=' + API_KEY + '&movieCd=' + movieCd);
                const data = await response.json();
                
                if (data.movieInfoResult && data.movieInfoResult.movieInfo) {
                    displayMovieDetail(data.movieInfoResult.movieInfo);
                } else {
                    container.innerHTML = '<div class="error">❌ 영화 정보를 불러올 수 없습니다.</div>';
                }
            } catch (error) {
                container.innerHTML = '<div class="error">❌ 데이터를 불러오는 중 오류가 발생했습니다.</div>';
                console.error('Error:', error);
            }
        }
        
        // 영화 상세 정보 표시
        function displayMovieDetail(movie) {
            const container = document.getElementById('movieDetail');
            
            // 감독 정보
            const directors = movie.directors.map(function(d) { return d.peopleNm; }).join(', ') || '정보 없음';
            
            // 배우 정보
            const actors = movie.actors.slice(0, 5).map(function(a) { return a.peopleNm; }).join(', ') || '정보 없음';
            
            // 장르 정보
            const genres = movie.genres.map(function(g) { return g.genreNm; }).join(', ') || '정보 없음';
            
            // 제작국가
            const nations = movie.nations.map(function(n) { return n.nationNm; }).join(', ') || '정보 없음';
            
            // 관람등급
            const audits = movie.audits.map(function(a) { return a.watchGradeNm; }).join(', ') || '정보 없음';
            
            // 제작사
            const companies = movie.companys.filter(function(c) { return c.companyPartNm === '제작사'; })
                .map(function(c) { return c.companyNm; }).join(', ') || '정보 없음';
            
            let html = '';
            html += '<div class="movie-header">';
            html += '<h1>' + movie.movieNm + '</h1>';
            html += '<div class="subtitle">' + (movie.movieNmEn || '') + '</div>';
            html += '</div>';
            
            html += '<div class="info-grid">';
            html += '<div class="info-card">';
            html += '<h3>📋 기본 정보</h3>';
            html += '<div class="info-item"><strong>개봉일:</strong> ' + (movie.openDt || '미개봉') + '</div>';
            html += '<div class="info-item"><strong>상영시간:</strong> ' + (movie.showTm ? movie.showTm + '분' : '정보 없음') + '</div>';
            html += '<div class="info-item"><strong>제작연도:</strong> ' + (movie.prdtYear || '정보 없음') + '년</div>';
            html += '<div class="info-item"><strong>영화유형:</strong> ' + (movie.typeNm || '정보 없음') + '</div>';
            html += '</div>';
            
            html += '<div class="info-card">';
            html += '<h3>🎬 장르 & 등급</h3>';
            html += '<div class="info-item"><strong>장르:</strong> ' + genres + '</div>';
            html += '<div class="info-item"><strong>관람등급:</strong> ' + audits + '</div>';
            html += '<div class="info-item"><strong>제작국가:</strong> ' + nations + '</div>';
            html += '<div class="info-item"><strong>제작상태:</strong> ' + (movie.prdtStatNm || '정보 없음') + '</div>';
            html += '</div>';
            html += '</div>';
            
            html += '<div class="detail-section">';
            html += '<h2>🎭 제작진 정보</h2>';
            html += '<div class="detail-content">';
            html += '<div class="stat-row">';
            html += '<span class="stat-label">감독</span>';
            html += '<span class="stat-value">' + directors + '</span>';
            html += '</div>';
            html += '<div class="stat-row">';
            html += '<span class="stat-label">주요 출연진</span>';
            html += '<span class="stat-value">' + actors + '</span>';
            html += '</div>';
            html += '<div class="stat-row">';
            html += '<span class="stat-label">제작사</span>';
            html += '<span class="stat-value">' + companies + '</span>';
            html += '</div>';
            html += '</div>';
            html += '</div>';
            
            html += '<div class="detail-section">';
            html += '<h2>ℹ️ 추가 정보</h2>';
            html += '<div class="detail-content">';
            html += '<div class="info-item"><strong>영화코드:</strong> ' + movie.movieCd + '</div>';
            if (movie.audits.length > 0) {
                html += '<div class="info-item" style="margin-top: 15px;">';
                html += '<strong>상세 관람등급 정보:</strong><br/>';
                for (let i = 0; i < movie.audits.length; i++) {
                    html += '• ' + movie.audits[i].watchGradeNm + ' (' + movie.audits[i].auditNo + ')<br/>';
                }
                html += '</div>';
            }
            html += '</div>';
            html += '</div>';
            
            container.innerHTML = html;
        }
        
        // 페이지 로드 시 실행
        window.addEventListener('DOMContentLoaded', function() {
            fetchMovieDetail();
        });
    </script>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</body>
</html>