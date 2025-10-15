package kr.or.hamaeyu.controller;

import java.io.IOException;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;
import kr.or.hamaeyu.dto.MovieBoardWrite;

@WebServlet("/movieReviewEdit")
public class MovieReviewEditServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        int id = Integer.parseInt(request.getParameter("id"));
        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        MovieBoardWrite review = dao.getReviewDetail(id);

        request.setAttribute("review", review);
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/movieReviewEdit.jsp");
        rd.forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");

        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String shortReview = request.getParameter("shortReview");
        String review = request.getParameter("review");
        int starRating = Integer.parseInt(request.getParameter("starRating"));

        MovieBoardWrite post = MovieBoardWrite.builder()
                .id(id)
                .title(title)
                .shortReview(shortReview)
                .review(review)
                .starRating(starRating)
                .build();

        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        int result = dao.updateReview(post);

        if (result > 0) {
            response.sendRedirect("movieReviewDetail?id=" + id);
        } else {
            request.setAttribute("errorMsg", "수정 실패");
            RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/movieReviewEdit.jsp");
            rd.forward(request, response);
        }
    }
}
