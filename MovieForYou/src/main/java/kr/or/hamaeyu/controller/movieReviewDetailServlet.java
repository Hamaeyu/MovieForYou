package kr.or.hamaeyu.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;
import kr.or.hamaeyu.dto.MovieBoardWrite;

import java.io.IOException;

@WebServlet("/movieReviewDetail")
public class movieReviewDetailServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    public movieReviewDetailServlet() {
        super();
    }
    
    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	String idStr = request.getParameter("id");
        if (idStr == null) {
            response.sendRedirect("movieReview");
            return;
        }

        int id = Integer.parseInt(idStr);
        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        MovieBoardWrite review = dao.getReviewDetail(id);

        if (review == null) {
            response.sendRedirect("movieReview");
            return;
        }

        request.setAttribute("review", review);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/movieReviewDetail.jsp");
        rd.forward(request, response);
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
