<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>리뷰 수정 - ${post.title}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cinemaReview.css">
  <link href="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/WEB-INF/views/header.jsp"/>

<div class="page-wrapper">
  <div class="cinema-container">
    <h2>🎬 영화관 후기 수정</h2>

    <form method="post" action="${pageContext.request.contextPath}/update.cinema">
      <input type="hidden" name="id" value="${post.id}">

      <div class="form-group">
        <label>제목</label>
        <input type="text" name="title" value="${post.title}" required>
      </div>

      <div class="form-group">
        <label>상영관</label>
        <input type="text" value="${post.cinemaName} (${post.brandName} / ${post.regionName})" disabled>
        <input type="hidden" name="cinema_id" value="${post.cinemaId}">
      </div>

      <div class="form-group">
        <label>별점</label>
        <div style="display:flex; gap:8px;">
          <select name="cinema_rating" class="check-btn">
            <c:forEach begin="1" end="5" var="i">
              <option value="${i}" ${i == post.cinemaRating ? 'selected' : ''}>${i}</option>
            </c:forEach>
          </select>
          <select name="seat_rating" class="check-btn">
            <c:forEach begin="1" end="5" var="i">
              <option value="${i}" ${i == post.seatRating ? 'selected' : ''}>${i}</option>
            </c:forEach>
          </select>
        </div>
      </div>

      <div class="form-group">
        <label>관람 시간</label>
        <input type="text" name="view_time" value="${post.viewTime}" placeholder="YYYY-MM-DD HH:MM">
      </div>

      <div class="form-group">
        <label>총평</label>
        <input type="text" name="overall_review" maxlength="100" value="${post.overallReview}">
      </div>

      <div class="form-group">
        <label>상세 후기 (서머노트)</label>
        <textarea id="summernote" name="content">${post.content}</textarea>
      </div>

      <button class="signup-btn" type="submit">수정 완료</button>
    </form>
  </div>
</div>

<script src="https://cdnjs.cloudflare.com/ajax/libs/jquery/3.6.4/jquery.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/summernote-lite.min.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
<script>
$(function(){
  $('#summernote').summernote({
    height: 350, lang: 'ko-KR',
    placeholder: '후기 내용을 수정하세요...',
    toolbar: [
      ['style', ['bold', 'italic', 'underline', 'clear']],
      ['font', ['fontsize', 'color']],
      ['para', ['ul', 'ol', 'paragraph']],
      ['insert', ['picture', 'link', 'video']],
      ['view', ['fullscreen', 'codeview']]
    ],
    callbacks: {
      onImageUpload: function(files){
        const fd = new FormData();
        fd.append("file", files[0]);
        $.ajax({
          url: APP_CTX + "/uploadImage.cinema", method: "POST", data: fd,
          processData: false, contentType: false,
          success: function(res){ $('#summernote').summernote('insertImage', res.url); },
          error: function(){ alert("이미지 업로드 실패"); }
        });
      }
    }
  });
});
</script>
</body>
</html>
