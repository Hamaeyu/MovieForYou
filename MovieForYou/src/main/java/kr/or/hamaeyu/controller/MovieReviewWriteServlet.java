package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;
import kr.or.hamaeyu.dto.MovieBoardWrite;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@WebServlet("/uploadMovieReview")
@MultipartConfig(maxFileSize = 10 * 1024 * 1024)
public class MovieReviewWriteServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public MovieReviewWriteServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
        // form 데이터 받기
        String title = request.getParameter("reviewName");
        String dateStr = request.getParameter("watchDate");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm");
        LocalDateTime dateTime = LocalDateTime.parse(dateStr, formatter);
        int rating = Integer.parseInt(request.getParameter("rating"));
        String review = request.getParameter("review");

        // DTO 객체에 담기
        MovieBoardWrite post = new MovieBoardWrite().builder().reviewName(title).watchDate(dateTime).rating(rating).review(review).build();

        // DAO 호출 (DB 저장)
        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        int result = dao.insertReviewPost(post);
        System.out.println(result);
        // 저장 후 결과 페이지로 이동
        if (result > 0) {
            response.sendRedirect("/WEB-INF/views/movieReviewBoard.jsp");
        }
	
	}

}
