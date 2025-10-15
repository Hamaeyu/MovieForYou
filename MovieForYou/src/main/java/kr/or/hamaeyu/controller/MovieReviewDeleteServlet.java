package kr.or.hamaeyu.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;

@WebServlet("/movieReviewDelete")
public class MovieReviewDeleteServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String idStr = request.getParameter("id");

        if (idStr == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 요청입니다.");
            return;
        }

        int id = Integer.parseInt(idStr);
        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        int result = dao.deleteReviewPost(id);

        if (result > 0) {
            response.sendRedirect("movieReview"); // 목록 페이지로 이동
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "삭제 실패");
        }
    }
}

