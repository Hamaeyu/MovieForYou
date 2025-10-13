<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>CineForum - Write a New Post</title>
    <link rel="stylesheet" href="<c:url value='/css/style.css'/>">
    <style>
        /* ✅ 전체 페이지 다크 배경 */
        body {
            background-color: #121212;
            color: #e0e0e0;
            font-family: 'Noto Sans KR', sans-serif;
            margin: 0;
            padding: 0;
        }

        main {
            max-width: 900px;
            margin: 50px auto;
        }

        h2 {
            margin-top: 30px;
            color: #ffffff;
        }

        p {
            color: #aaaaaa;
            margin-bottom: 20px;
        }

        form {
            background-color: #1e1e1e; /* ✅ 폼 컨테이너 밝은 블랙 */
            padding: 30px;
            border-radius: 12px;
            box-shadow: 0 4px 12px rgba(0,0,0,0.4);
        }

        label {
            display: block;
            margin-top: 16px;
            font-weight: 600;
            color: #ccc;
        }

        input[type="text"],
        input[type="datetime-local"],
        input[type="number"],
        input[type="file"],
        textarea {
            width: 100%;
            margin-top: 8px;
            padding: 12px;
            border: none;
            border-radius: 8px;
            background-color: #2b2b2b;
            color: #fff;
            font-size: 15px;
        }

        textarea::placeholder,
        input::placeholder {
            color: #888;
        }

        button {
            border: none;
            border-radius: 8px;
            padding: 10px 18px;
            font-weight: 600;
            cursor: pointer;
            transition: 0.2s ease;
        }

        button:hover {
            opacity: 0.9;
        }

        .btn-back {
            background: #333;
            color: #fff;
            margin-bottom: 20px;
        }

        .btn-draft {
            background: #444;
            color: #fff;
        }

        .btn-preview {
            background: #FFD43B;
            color: #000;
        }

        .btn-publish {
            background: #00c46f;
            color: #fff;
        }

        .btn-area {
            display: flex;
            justify-content: flex-end;
            gap: 10px;
            margin-top: 25px;
        }
    </style>
</head>
<body>

    <!-- 헤더 -->
	<jsp:include page="/WEB-INF/views/header.jsp" />

    <main>
        <a href="movieReview">
            <button class="btn-back">← Back to Reviews</button>
        </a>

        <h2>Write a New Post</h2>
        <p>Share your thoughts about movies with the community</p>

        <form action="/movieReview" method="post" enctype="multipart/form-data">

            <label>관람일시 *</label>
            <input type="datetime-local" name="watchDate">

            <label>별점</label>
            <input type="number" name="rating" min="1" max="10" placeholder="1~10점">

            <label>영화 후기</label>
            <textarea name="review" rows="8" placeholder="Write your review or thoughts here..."></textarea>

            <label style="margin-top:30px;">Add Poll (Optional)</label>
            <input type="text" name="pollQuestion" placeholder="Poll Question">
            <input type="text" name="pollOption1" placeholder="Excellent">
            <input type="text" name="pollOption2" placeholder="Good">

            <label style="margin-top:30px;">Image</label>
            <input type="file" name="imageFile" accept="image/*">

            <div class="btn-area">
                <button type="button" class="btn-draft">Save Draft</button>
                <button type="button" class="btn-preview">Preview</button>
                <button type="submit" class="btn-publish">Publish Post</button>
            </div>
        </form>
    </main>

</body>
</html>
