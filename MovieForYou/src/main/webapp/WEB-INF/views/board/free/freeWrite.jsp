<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>자유 게시판</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/freeWrite.css">
	<script src="https://cdn.ckeditor.com/ckeditor5/38.0.1/classic/ckeditor.js"></script>
</head>
<body>
	<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/header.jsp"></jsp:include>
	<div class="free-container">
		<!-- Main Content -->
		<main>
			<div class="page-header">
				<h1>🍿자유 게시판</h1>
				<p>영화와 관련된 글을 자유롭게 작성하세요.</p>
			</div>

			<div class="form-container">
				<form id="postForm">
					<!-- Title -->
					<div class="form-group">
						<label for="title">제목 <span class="required">*</span></label> 
						<input type="text" id="title" placeholder="제목을 입력하세요."required>
					</div>
					<!-- Content -->
					<div class="form-group">
						<label for="content">내용 <span class="required">*</span></label>
						<textarea name="content" id="editor" required
							placeholder="내용을 입력하세요.">
						</textarea>
					</div>
					<!-- Submit Button -->
					<div class="button-wrapper">
						<button type="submit" class="submit-btn">
							<span>✓</span> <span>등록</span>
						</button>
					</div>
				</form>
			</div>
			
		</main>
	</div>
	<jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/footer.jsp"></jsp:include>

</body>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/freeWrite.js"></script>
</html>