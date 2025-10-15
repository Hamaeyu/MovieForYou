package kr.or.hamaeyu.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminCinemaDao;
import kr.or.hamaeyu.dto.CinemaFullInfo;

@WebServlet("/admin/more")
public class MoreCinemaServlet extends HttpServlet {
  @Override
  protected void doGet(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    int page = Integer.parseInt(req.getParameter("page"));
    int limit = 3;
    int offset = (page - 1) * limit;

    AdminCinemaDao dao = new AdminCinemaDao();
    List<CinemaFullInfo> moreList = dao.getPagedTheaters(offset, limit);

    if (moreList == null || moreList.isEmpty()) {
      resp.getWriter().write(""); // ✅ 완전한 빈 응답
      return;
    }

    req.setAttribute("cinemaList", moreList); // ✅ 이름 통일
    RequestDispatcher rd = req.getRequestDispatcher("/WEB-INF/views/cinemaList.jsp");
    rd.forward(req, resp);
  }
}

