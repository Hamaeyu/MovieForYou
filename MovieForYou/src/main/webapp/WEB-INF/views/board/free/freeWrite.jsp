<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
	<meta charset="UTF-8">
	<title>자유 게시판</title>
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/freeWrite.css">
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
						<textarea id="content" rows="20"
							placeholder="내용을 입력하세요." required></textarea>
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
	<script>
        // Image file upload handler
        document.getElementById('imageFile').addEventListener('change', function(e) {
            const file = e.target.files[0];
            if (file) {
                document.getElementById('uploadText').textContent = file.name;
            }
        });

        // Form submit handler
        document.getElementById('postForm').addEventListener('submit', function(e) {
            e.preventDefault();
            
            const formData = {
                title: document.getElementById('title').value,
                category: document.getElementById('category').value,
                movieSearch: document.getElementById('movieSearch').value,
                content: document.getElementById('content').value,
                image: document.getElementById('imageFile').files[0]?.name || null
            };
            
            console.log('Form submitted:', formData);
            alert('게시글이 등록되었습니다!');
        });

        // Back button handler
        document.querySelector('.back-btn').addEventListener('click', function() {
            window.history.back();
        });
    </script>
</body>
</html>