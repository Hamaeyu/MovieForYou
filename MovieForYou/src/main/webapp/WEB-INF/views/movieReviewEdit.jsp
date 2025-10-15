<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>리뷰 수정</title>
  
  <!-- ✅ jQuery -->
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>

  <!-- ✅ Summernote -->
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/lang/summernote-ko-KR.min.js"></script>

  <style>
    body {
      background-color: #121212;
      color: #e0e0e0;
      font-family: 'Noto Sans KR', sans-serif;
      margin: 0;
      padding: 0;
    }

    .form-container {
      max-width: 900px;
      margin: 50px auto;
    }

    form {
      background-color: #1e1e1e;
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

    .note-editor.note-frame {
      background-color: #fff !important;
      color: #000 !important;
    }

    .note-toolbar {
      background-color: #f7f7f7 !important;
      border-bottom: 1px solid #ddd !important;
    }
  </style>
</head>

<body>

<div class="form-container">
  <h2>리뷰 수정</h2>

  <form action="movieReviewEdit" method="post">
    <input type="hidden" name="id" value="${review.id}">

    <label>제목</label>
    <input type="text" name="title" value="${review.title}" required>

    <label>별점 (1~10)</label>
    <input type="number" name="starRating" min="1" max="10" value="${review.starRating}" required>

    <label>한줄평</label>
    <input type="text" name="shortReview" value="${review.shortReview}">

    <label>상세 후기</label>
    <!-- ✅ id 추가 -->
    <textarea id="review" name="review" rows="8">${review.review}</textarea>

    <div class="btn-area">
      <button type="submit" class="btn-publish">저장하기</button>
      <a href="movieReviewDetail?id=${review.id}" style="color:#aaa; margin-left:10px;">취소</a>
    </div>
  </form>
</div>

<!-- ✅ 스크립트는 body 맨 아래 -->
<script>
$(document).ready(function() {
  console.log("✅ jQuery loaded, DOM ready");

  // ✅ Summernote 초기화
  $('#review').summernote({
    height: 300,
    lang: 'ko-KR',
    placeholder: '영화 후기를 입력하세요...',
    toolbar: [
      ['style', ['bold', 'italic', 'underline', 'clear']],
      ['font', ['fontsize', 'color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['insert', ['link', 'picture']],
      ['view', ['fullscreen', 'codeview', 'help']]
    ]
  });

  // ✅ 기존 내용 (HTML 포함) 복원
  const content = $('#review').val();
  $('#review').summernote('code', content);
});
</script>

</body>
</html>
