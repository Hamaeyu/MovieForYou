<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Movie</title>
    <style>
    * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        .movie-list-page {
            margin: 0;
            padding: 0;
            background-color: #1a1a1a;
            font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
            color: #fff;
            min-height: 100vh;
        }

        .movie-list-container {
            max-width: 1400px;
            margin: 0 auto;
            padding: 40px 20px;
        }

        .page-header {
            margin-bottom: 40px;
        }

        .page-title {
            font-size: 36px;
            font-weight: 700;
            margin-bottom: 20px;
        }

        .filters-row {
            display: flex;
            gap: 15px;
            flex-wrap: wrap;
            margin-bottom: 40px;
        }

        .filter-dropdown {
            position: relative;
        }

        .filter-btn {
            background: rgba(255, 255, 255, 0.1);
            border: 1px solid rgba(255, 255, 255, 0.2);
            color: #fff;
            padding: 10px 20px;
            border-radius: 8px;
            cursor: pointer;
            font-size: 14px;
            transition: all 0.3s ease;
            display: flex;
            align-items: center;
            gap: 8px;
        }

        .filter-btn:hover {
            background: rgba(255, 255, 255, 0.2);
            border-color: rgba(255, 255, 255, 0.3);
        }

        .movies-grid {
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(220px, 1fr));
            gap: 30px;
            margin-bottom: 50px;
        }

        .movie-card {
            background: rgba(255, 255, 255, 0.05);
            border-radius: 12px;
            overflow: hidden;
            cursor: pointer;
            transition: all 0.3s ease;
            border: 1px solid rgba(255, 255, 255, 0.1);
            position: relative;
        }

        .movie-card:hover {
            transform: translateY(-8px);
            box-shadow: 0 20px 40px rgba(0, 0, 0, 0.5);
            border-color: rgba(255, 255, 255, 0.2);
        }

        .movie-poster {
            width: 100%;
            height: 320px;
            background: linear-gradient(135deg, rgba(255, 255, 255, 0.1), rgba(255, 255, 255, 0.05));
            position: relative;
            overflow: hidden;
            display: flex;
            align-items: center;
            justify-content: center;
            color: rgba(255, 255, 255, 0.3);
            font-size: 14px;
        }

        .movie-poster img {
            width: 100%;
            height: 100%;
            object-fit: cover;
        }

        .rating-badge {
            position: absolute;
            top: 12px;
            left: 12px;
            background: #ffd43b;
            color: #1a1a1a;
            padding: 6px 12px;
            border-radius: 20px;
            font-size: 13px;
            font-weight: 700;
            display: flex;
            align-items: center;
            gap: 4px;
            z-index: 1;
        }

        .rating-badge.high {
            background: #4caf50;
            color: #fff;
        }

        .rating-badge.medium {
            background: #ffd43b;
            color: #1a1a1a;
        }

        .rating-badge.low {
            background: #ff6b6b;
            color: #fff;
        }

        .movie-info {
            padding: 20px;
        }

        .movie-title {
            font-size: 16px;
            font-weight: 600;
            margin-bottom: 8px;
            color: #fff;
            display: -webkit-box;
            -webkit-line-clamp: 2;
            -webkit-box-orient: vertical;
            overflow: hidden;
            line-height: 1.4;
        }

        .movie-meta {
            display: flex;
            justify-content: space-between;
            align-items: center;
            font-size: 13px;
            color: rgba(255, 255, 255, 0.6);
            margin-bottom: 12px;
        }

        .movie-genre {
            display: flex;
            gap: 5px;
            flex-wrap: wrap;
        }

        .genre-tag {
            background: rgba(255, 255, 255, 0.1);
            padding: 4px 10px;
            border-radius: 12px;
            font-size: 11px;
        }

        .movie-stats {
            display: flex;
            gap: 15px;
            align-items: center;
            font-size: 13px;
            color: rgba(255, 255, 255, 0.6);
        }

        .stat-item {
            display: flex;
            align-items: center;
            gap: 5px;
        }

        .load-more-btn {
            display: block;
            margin: 0 auto;
            background: #ffd43b;
            color: #1a1a1a;
            border: none;
            padding: 15px 50px;
            border-radius: 30px;
            font-size: 16px;
            font-weight: 700;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .load-more-btn:hover {
            background: #ffdd5e;
            transform: scale(1.05);
            box-shadow: 0 10px 30px rgba(255, 212, 59, 0.3);
        }

        .star-icon {
            color: #ffd43b;
        }

        @media (max-width: 768px) {
            .movies-grid {
                grid-template-columns: repeat(auto-fill, minmax(150px, 1fr));
                gap: 20px;
            }

            .movie-poster {
                height: 240px;
            }

            .page-title {
                font-size: 28px;
            }
        }
    </style>
</head>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
<body>
    <div class="movie-list-page">
        <div class="movie-list-container">
            <div class="page-header">
                <h1 class="page-title">Discover Movies</h1>
            </div>

            <div class="filters-row">
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        All Genres ▼
                    </button>
                </div>
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        Any Year ▼
                    </button>
                </div>
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        Rating 7+ ▼
                    </button>
                </div>
                <div class="filter-dropdown">
                    <button class="filter-btn">
                        Popularity ▼
                    </button>
                </div>
            </div>

            <div class="movies-grid">
                <!-- Movie Card 1 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 9.0</div>
                        <span>The Dark Knight</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">The Dark Knight</h3>
                        <div class="movie-meta">
                            <span>Action, Crime, Drama</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 2.3M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 2 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 8.8</div>
                        <span>Inception</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">Inception</h3>
                        <div class="movie-meta">
                            <span>Sci-Fi, Thriller</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 1.8M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 3 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge medium">★ 8.9</div>
                        <span>Pulp Fiction</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">Pulp Fiction</h3>
                        <div class="movie-meta">
                            <span>Crime, Drama</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 1.6M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 4 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 8.6</div>
                        <span>Interstellar</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">Interstellar</h3>
                        <div class="movie-meta">
                            <span>Sci-Fi, Adventure</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 1.3M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 5 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 8.8</div>
                        <span>Parasite</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">Parasite</h3>
                        <div class="movie-meta">
                            <span>Comedy, Drama, Thriller</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 1.9M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 6 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge medium">★ 8.5</div>
                        <span>The Godfather</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">The Godfather</h3>
                        <div class="movie-meta">
                            <span>Crime, Drama</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 1.8M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 7 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 8.4</div>
                        <span>Avengers: Endgame</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">Avengers: Endgame</h3>
                        <div class="movie-meta">
                            <span>Action, Adventure, Drama</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 2.7M</span>
                        </div>
                    </div>
                </div>

                <!-- Movie Card 8 -->
                <div class="movie-card">
                    <div class="movie-poster">
                        <div class="rating-badge high">★ 9.3</div>
                        <span>The Shawshank Redemption</span>
                    </div>
                    <div class="movie-info">
                        <h3 class="movie-title">The Shawshank Redemption</h3>
                        <div class="movie-meta">
                            <span>Drama</span>
                        </div>
                        <div class="movie-stats">
                            <span class="stat-item">👁 2.8M</span>
                        </div>
                    </div>
                </div>
            </div>

            <button class="load-more-btn">Load More Movies</button>
        </div>
    </div>
<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>
    <script>
        // 영화 카드 클릭 이벤트
        document.querySelectorAll('.movie-card').forEach(card => {
            card.addEventListener('click', function() {
                console.log('영화 상세 페이지로 이동');
                // window.location.href = '/movie-detail?id=' + movieId;
            });
        });

        // Load More 버튼
        document.querySelector('.load-more-btn')?.addEventListener('click', function() {
            console.log('더 많은 영화 로드');
            // AJAX 요청으로 추가 영화 데이터 가져오기
        });
    </script>
</body>
</html>