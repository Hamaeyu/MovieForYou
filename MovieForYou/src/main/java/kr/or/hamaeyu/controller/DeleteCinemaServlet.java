package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminCinemaDao;

import java.io.IOException;

@WebServlet("/admin/deleteCinema")
public class DeleteCinemaServlet extends HttpServlet {
	@Override
	  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
	      throws ServletException, IOException {

	    req.setCharacterEncoding("UTF-8");

	    int id = Integer.parseInt(req.getParameter("id"));

	    AdminCinemaDao dao = new AdminCinemaDao();
	    int result = dao.deleteTheater(id);

	    if (result > 0) {
	      resp.sendRedirect(req.getContextPath() + "/admin");
	    } else {
	      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "삭제 실패");
	    }
	  }

}
