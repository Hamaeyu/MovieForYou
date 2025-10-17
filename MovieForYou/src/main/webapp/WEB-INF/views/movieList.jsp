<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>MovieForYou - 일일 박스오피스</title>
    <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
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
        
        .main-content {
            max-width: 1400px;
            margin: 0 auto;
            padding: 40px 20px;
        }
        
        .container {
            background: rgba(0, 0, 0, 0.4);
            backdrop-filter: blur(20px);
            border-radius: 20px;
            padding: 40px;
            box-shadow: 0 20px 60px rgba(0, 0, 0, 0.5);
            border: 1px solid rgba(255, 255, 255, 0.1);
            margin-bottom: 40px;
        }
        
        h1 {
            text-align: center;
            color: #ffd700;
            margin-bottom: 30px;
            font-size: 2.5em;
            text-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
        }
        
        .chart-section {
            margin-bottom: 40px;
            padding: 30px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            border: 1px solid rgba(255, 215, 0, 0.2);
        }
        
        .chart-section h2 {
            color: #ffd700;
            margin-bottom: 25px;
            font-size: 1.8em;
            text-align: center;
        }
        
        .chart-container {
            position: relative;
            height: 300px;
            margin-bottom: 20px;
        }
        
        .search-area {
            display: flex;
            gap: 15px;
            margin-bottom: 40px;
            flex-wrap: wrap;
            padding: 20px;
            background: rgba(255, 255, 255, 0.05);
            border-radius: 15px;
            border: 1px solid rgba(255, 215, 0, 0.2);
        }
        
        .search-area input {
            flex: 1;
            min-width: 200px;
            padding: 15px 25px;
            border: 2px solid rgba(255, 215, 0, 0.3);
            border-radius: 30px;
            font-size: 16px;
            background: rgba(255, 255, 255, 0.1);
            color: #fff;
            transition: all 0.3s;
        }
        
        .search-area input::placeholder {
            color: rgba(255, 255, 255, 0.5);
        }
        
        .search-area input:focus {
            outline: none;
            border-color: #ffd700;
            background: rgba(255, 255, 255, 0.15);
            box-shadow: 0 0 20px rgba(255, 215, 0, 0.3);
        }
        
        .search-area button {
            padding: 15px 40px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a2e;
            border: none;
            border-radius: 30px;
            cursor: pointer;
            font-size: 16px;
            font-weight: bold;
            transition: all 0.3s;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.3);
        }
        
        .search-area button:hover {
            transform: translateY(-2px);
            box-shadow: 0 8px 25px rgba(255, 215, 0, 0.5);
        }
        
        .movie-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
            gap: 30px;
            margin-bottom: 40px;
        }
        
        .movie-card {
            background: linear-gradient(145deg, rgba(255, 255, 255, 0.1) 0%, rgba(255, 255, 255, 0.05) 100%);
            border-radius: 15px;
            padding: 0;
            cursor: pointer;
            transition: all 0.4s;
            position: relative;
            overflow: hidden;
            border: 1px solid rgba(255, 215, 0, 0.2);
        }
        
        .movie-card:hover {
            transform: translateY(-10px) scale(1.02);
            box-shadow: 0 15px 40px rgba(255, 215, 0, 0.4);
            border-color: #ffd700;
        }
        
        .movie-poster {
            width: 100%;
            height: 400px;
            position: relative;
            overflow: hidden;
            background: linear-gradient(135deg, #2c3e50 0%, #34495e 100%);
        }
        
        .movie-poster img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }
        
        .rank-badge {
            position: absolute;
            top: 15px;
            left: 15px;
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a2e;
            width: 50px;
            height: 50px;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            font-weight: bold;
            font-size: 20px;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.5);
            z-index: 10;
        }
        
        .movie-content {
            padding: 20px;
        }
        
        .movie-title {
            font-size: 1.3em;
            font-weight: bold;
            margin-bottom: 12px;
            color: #ffd700;
            text-shadow: 0 2px 10px rgba(255, 215, 0, 0.3);
        }
        
        .movie-info {
            color: rgba(255, 255, 255, 0.8);
            margin: 8px 0;
            font-size: 0.95em;
        }
        
        .movie-info strong {
            color: #ffd700;
        }
        
        .rank-change {
            display: inline-block;
            margin-left: 5px;
            font-weight: bold;
        }
        
        .rank-up {
            color: #ff6b6b;
        }
        
        .rank-down {
            color: #4ecdc4;
        }
        
        .rank-same {
            color: #95a5a6;
        }
        
        .pagination {
            display: flex;
            justify-content: center;
            gap: 12px;
            margin-top: 40px;
        }
        
        .pagination button {
            padding: 12px 20px;
            border: 2px solid rgba(255, 215, 0, 0.5);
            background: rgba(255, 255, 255, 0.1);
            color: #ffd700;
            border-radius: 10px;
            cursor: pointer;
            transition: all 0.3s;
            font-weight: bold;
            backdrop-filter: blur(10px);
        }
        
        .pagination button:hover:not(:disabled) {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a2e;
            transform: translateY(-2px);
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }
        
        .pagination button.active {
            background: linear-gradient(135deg, #ffd700 0%, #ffed4e 100%);
            color: #1a1a2e;
            box-shadow: 0 5px 15px rgba(255, 215, 0, 0.4);
        }
        
        .pagination button:disabled {
            opacity: 0.3;
            cursor: not-allowed;
        }
        
        .loading, .error, .no-results {
            text-align: center;
            padding: 80px 20px;
            font-size: 1.3em;
        }
        
        .loading {
            color: #ffd700;
            text-shadow: 0 0 20px rgba(255, 215, 0, 0.5);
        }
        
        .error {
            color: #ff6b6b;
        }
        
        .no-results {
            color: rgba(255, 255, 255, 0.7);
        }

        .new-badge {
            display: inline-block;
            padding: 3px 8px;
            background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
            color: white;
            border-radius: 5px;
            font-size: 0.85em;
            font-weight: bold;
            margin-left: 8px;
            box-shadow: 0 2px 8px rgba(255, 107, 107, 0.4);
        }
        
    </style>
</head>

<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
<body>
    <div class="main-content">
        <div class="container">
            <h1>📊 일일 박스오피스</h1>
            
            <div class="search-area">
                <input type="date" id="dateInput" autocomplete="off" />
                <input type="text" id="searchInput" placeholder="영화 제목으로 검색..." autocomplete="off" />
                <button onclick="searchMovies()">🔍 검색</button>
            </div>
            
            <div id="charts" class="chart-section">
                <h2>📈 관객수 Top 10</h2>
                <div class="chart-container">
                    <canvas id="audienceChart"></canvas>
                </div>
                <h2>💰 매출액 Top 10</h2>
                <div class="chart-container">
                    <canvas id="salesChart"></canvas>
                </div>
            </div>
            
            <div id="movies">
                <h2 style="color: #ffd700; margin-bottom: 30px; text-align: center; font-size: 2em;">🎥 영화 목록</h2>
                <div id="movieContainer"></div>
            </div>
            
            <div class="pagination" id="pagination"></div>
        </div>
    </div>

    <script>
        const API_KEY = 'f19b8399de089df31b5c4de5101998d0';
        const API_URL = 'http://kobis.or.kr/kobisopenapi/webservice/rest/boxoffice/searchDailyBoxOfficeList.json';
        
        let allMovies = [];
        let filteredMovies = [];
        let currentPage = 1;
        const itemsPerPage = 8;
        let audienceChart = null;
        let salesChart = null;
        
        // Unsplash API를 통한 랜덤 영화 이미지
        function getRandomMovieImage(index) {
            const seeds = ['cinema', 'movie', 'film', 'theater', 'hollywood', 'entertainment', 'drama', 'action'];
            const seed = seeds[index % seeds.length];
            return 'https://source.unsplash.com/400x600/?' + seed + '&sig=' + index;
        }
        
        // 어제 날짜로 초기화
        function initializeDate() {
            const yesterday = new Date();
            yesterday.setDate(yesterday.getDate() - 1);
            const dateString = yesterday.toISOString().split('T')[0];
            document.getElementById('dateInput').value = dateString;
        }
        
        // 날짜 형식 변환
        function formatDate(dateString) {
            return dateString.replace(/-/g, '');
        }
        
        // 숫자 포맷팅
        function formatNumber(num) {
            return num.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
        }
        
        // 순위 변동 표시
        function getRankChange(rankInten) {
            const change = parseInt(rankInten);
            if (change > 0) {
                return '<span class="rank-change rank-up">▲ ' + change + '</span>';
            } else if (change < 0) {
                return '<span class="rank-change rank-down">▼ ' + Math.abs(change) + '</span>';
            } else {
                return '<span class="rank-change rank-same">-</span>';
            }
        }
        
        // 차트 생성
        function createCharts(movies) {
            const top10 = movies.slice(0, 10);
            const labels = top10.map(function(m) { return m.movieNm; });
            const audienceData = top10.map(function(m) { return parseInt(m.audiCnt); });
            const salesData = top10.map(function(m) { return parseInt(m.salesAmt) / 100000000; });
            
            // 관객수 차트
            const audienceCtx = document.getElementById('audienceChart').getContext('2d');
            if (audienceChart) {
                audienceChart.destroy();
            }
            audienceChart = new Chart(audienceCtx, {
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '관객수 (명)',
                        data: audienceData,
                        backgroundColor: 'rgba(255, 215, 0, 0.7)',
                        borderColor: 'rgba(255, 215, 0, 1)',
                        borderWidth: 2
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            labels: {
                                color: '#ffd700',
                                font: { size: 14 }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                color: '#ffd700',
                                callback: function(value) {
                                    return value.toLocaleString();
                                }
                            },
                            grid: {
                                color: 'rgba(255, 215, 0, 0.1)'
                            }
                        },
                        x: {
                            ticks: {
                                color: '#ffd700',
                                maxRotation: 45,
                                minRotation: 45
                            },
                            grid: {
                                color: 'rgba(255, 215, 0, 0.1)'
                            }
                        }
                    }
                }
            });
            
            // 매출액 차트
            const salesCtx = document.getElementById('salesChart').getContext('2d');
            if (salesChart) {
                salesChart.destroy();
            }
            salesChart = new Chart(salesCtx, {
                type: 'line',
                data: {
                    labels: labels,
                    datasets: [{
                        label: '매출액 (억원)',
                        data: salesData,
                        backgroundColor: 'rgba(78, 205, 196, 0.2)',
                        borderColor: 'rgba(78, 205, 196, 1)',
                        borderWidth: 3,
                        fill: true,
                        tension: 0.4
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    plugins: {
                        legend: {
                            labels: {
                                color: '#4ecdc4',
                                font: { size: 14 }
                            }
                        }
                    },
                    scales: {
                        y: {
                            beginAtZero: true,
                            ticks: {
                                color: '#4ecdc4',
                                callback: function(value) {
                                    return value.toFixed(1) + '억';
                                }
                            },
                            grid: {
                                color: 'rgba(78, 205, 196, 0.1)'
                            }
                        },
                        x: {
                            ticks: {
                                color: '#4ecdc4',
                                maxRotation: 45,
                                minRotation: 45
                            },
                            grid: {
                                color: 'rgba(78, 205, 196, 0.1)'
                            }
                        }
                    }
                }
            });
        }
        
        // 영화 데이터 가져오기
        async function fetchMovies() {
            const dateInput = document.getElementById('dateInput').value;
            const targetDt = formatDate(dateInput);
            const container = document.getElementById('movieContainer');
            
            container.innerHTML = '<div class="loading">🎬 영화 정보를 불러오는 중...</div>';
            
            try {
                const response = await fetch(API_URL + '?key=' + API_KEY + '&targetDt=' + targetDt);
                const data = await response.json();
                
                if (data.boxOfficeResult && data.boxOfficeResult.dailyBoxOfficeList) {
                    allMovies = data.boxOfficeResult.dailyBoxOfficeList;
                    filteredMovies = allMovies.slice();
                    currentPage = 1;
                    createCharts(allMovies);
                    displayMovies();
                } else {
                    container.innerHTML = '<div class="error">❌ 영화 데이터를 불러올 수 없습니다.</div>';
                }
            } catch (error) {
                container.innerHTML = '<div class="error">❌ 데이터를 불러오는 중 오류가 발생했습니다.</div>';
                console.error('Error:', error);
            }
        }
        
        // 영화 검색
        function searchMovies() {
            const searchTerm = document.getElementById('searchInput').value.toLowerCase();
            
            if (searchTerm === '') {
                filteredMovies = allMovies.slice();
            } else {
                filteredMovies = allMovies.filter(function(movie) {
                    return movie.movieNm.toLowerCase().indexOf(searchTerm) !== -1;
                });
            }
            
            currentPage = 1;
            displayMovies();
        }
        
        // 영화 목록 표시
        function displayMovies() {
            const container = document.getElementById('movieContainer');
            
            if (filteredMovies.length === 0) {
                container.innerHTML = '<div class="no-results">🔍 검색 결과가 없습니다.</div>';
                document.getElementById('pagination').innerHTML = '';
                return;
            }
            
            const startIndex = (currentPage - 1) * itemsPerPage;
            const endIndex = startIndex + itemsPerPage;
            const moviesToDisplay = filteredMovies.slice(startIndex, endIndex);
            
            let html = '<div class="movie-grid">';
            for (let i = 0; i < moviesToDisplay.length; i++) {
                const movie = moviesToDisplay[i];
                const newBadge = movie.rankOldAndNew === 'NEW' ? '<span class="new-badge">NEW</span>' : '';
                const posterUrl = getRandomMovieImage(parseInt(movie.rank));
                
                html += '<div class="movie-card" onclick="goToDetail(\'' + movie.movieCd + '\', \'' + movie.movieNm.replace(/'/g, "\\'") + '\')">';
                html += '<div class="rank-badge">' + movie.rank + '</div>';
                html += '<div class="movie-poster"><img src="' + posterUrl + '" alt="' + movie.movieNm + '" loading="lazy"></div>';
                html += '<div class="movie-content">';
                html += '<div class="movie-title">' + movie.movieNm + newBadge + '</div>';
                html += '<div class="movie-info">순위 변동: ' + getRankChange(movie.rankInten) + '</div>';
                html += '<div class="movie-info"><strong>개봉일:</strong> ' + movie.openDt + '</div>';
                html += '<div class="movie-info"><strong>관객수:</strong> ' + formatNumber(movie.audiCnt) + '명</div>';
                html += '<div class="movie-info"><strong>누적관객:</strong> ' + formatNumber(movie.audiAcc) + '명</div>';
                html += '</div>';
                html += '</div>';
            }
            html += '</div>';
            
            container.innerHTML = html;
            displayPagination();
        }
        
        // 페이지네이션 표시
        function displayPagination() {
            const totalPages = Math.ceil(filteredMovies.length / itemsPerPage);
            const paginationContainer = document.getElementById('pagination');
            
            let html = '';
            const prevDisabled = currentPage === 1 ? 'disabled' : '';
            html += '<button onclick="changePage(' + (currentPage - 1) + ')" ' + prevDisabled + '>◀ 이전</button>';
            
            for (let i = 1; i <= totalPages; i++) {
                const activeClass = i === currentPage ? 'active' : '';
                html += '<button onclick="changePage(' + i + ')" class="' + activeClass + '">' + i + '</button>';
            }
            
            const nextDisabled = currentPage === totalPages ? 'disabled' : '';
            html += '<button onclick="changePage(' + (currentPage + 1) + ')" ' + nextDisabled + '>다음 ▶</button>';
            
            paginationContainer.innerHTML = html;
        }
        
        // 페이지 변경
        function changePage(page) {
            const totalPages = Math.ceil(filteredMovies.length / itemsPerPage);
            if (page < 1 || page > totalPages) return;
            
            currentPage = page;
            displayMovies();
            document.getElementById('movies').scrollIntoView({ behavior: 'smooth' });
        }
        
        // 상세 페이지로 이동
        function goToDetail(movieCd, movieNm) {
            const dateInput = document.getElementById('dateInput').value;
            window.location.href = 'detail.jsp?movieCd=' + movieCd + '&movieNm=' + encodeURIComponent(movieNm) + '&date=' + dateInput;
        }
        
        // 이벤트 리스너
        document.getElementById('searchInput').addEventListener('keypress', function(e) {
            if (e.key === 'Enter') {
                searchMovies();
            }
        });
        
        document.getElementById('dateInput').addEventListener('change', function() {
            fetchMovies();
        });
        
        // 초기 로드
        window.addEventListener('DOMContentLoaded', function() {
            initializeDate();
            fetchMovies();
        });
    </script>
    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
</body>
</html>