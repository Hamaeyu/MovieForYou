package kr.or.hamaeyu.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import java.io.IOException;
import java.util.List;
import kr.or.hamaeyu.dao.MovieBoardWriteDao;
import kr.or.hamaeyu.dto.MovieBoardWrite;

@WebServlet("/movieReview")
public class MovieReviewBoardServlet extends HttpServlet {
    private static final int PAGE_SIZE = 5;  // 한 페이지당 게시글 수

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        // 페이지 번호 파라미터
        int page = 1;
        String pageParam = request.getParameter("page");
        if (pageParam != null) {
            try {
                page = Integer.parseInt(pageParam);
            } catch (NumberFormatException e) {
                page = 1;
            }
        }

        MovieBoardWriteDao dao = new MovieBoardWriteDao();
        int totalCount = dao.getReviewCount(); // 전체 게시글 수
        int totalPage = (int) Math.ceil((double) totalCount / PAGE_SIZE);

        // 현재 페이지 게시글 목록
        List<MovieBoardWrite> reviewList = dao.getReviewList(page, PAGE_SIZE);

        // JSP로 전달
        request.setAttribute("reviewList", reviewList);
        request.setAttribute("currentPage", page);
        request.setAttribute("totalPage", totalPage);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/movieReviewBoard.jsp");
        rd.forward(request, response);
    }
}
