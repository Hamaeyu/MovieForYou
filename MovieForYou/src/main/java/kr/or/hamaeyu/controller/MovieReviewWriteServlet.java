package kr.or.hamaeyu.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;
import kr.or.hamaeyu.dto.MovieBoardWrite;

import java.io.IOException;

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
        String title = request.getParameter("title");
        String shortReview = request.getParameter("shortReview");
        int rating = Integer.parseInt(request.getParameter("starRating"));
        String review = request.getParameter("review");

        // DTO 객체에 담기
        MovieBoardWrite post = new MovieBoardWrite().builder().title(title).starRating(rating).review(review).shortReview(shortReview).build();

        // DAO 호출 (DB 저장)
        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        
        HttpSession session = request.getSession(false);
        
        if (session == null || session.getAttribute("loginUserId") == null) {
            System.out.println("[세션 없음] 로그인 후 이용하세요.");
            response.sendRedirect("/login.auth");
            return;
        }
        long userId = (long) session.getAttribute("loginUserId");
        int result = dao.insertReviewPost(post, userId);
        System.out.println(result);
        // 저장 후 결과 페이지로 이동
        if (result > 0) {
        	response.sendRedirect(request.getContextPath() + "/movieReview");
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "등록 실패");
        }
	
	}

}
