package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminCinemaDao;

import java.io.IOException;

@WebServlet("/admin/updateCinema")
public class UpdateCinemaModalServlet extends HttpServlet {
  @Override
  protected void doPost(HttpServletRequest req, HttpServletResponse resp)
      throws ServletException, IOException {

    req.setCharacterEncoding("UTF-8");

    int id = Integer.parseInt(req.getParameter("id"));
    String name = req.getParameter("cinemaName");
    String address = req.getParameter("address");
    int typeId = Integer.parseInt(req.getParameter("type"));
    int regionId = Integer.parseInt(req.getParameter("region"));
    String latitude = req.getParameter("latitude");
    String longitude = req.getParameter("longitude");

    AdminCinemaDao dao = new AdminCinemaDao();
    int result = dao.updateTheater(id, name, address, latitude, longitude, typeId, regionId);

    if (result > 0) {
      resp.sendRedirect(req.getContextPath() + "/admin");
    } else {
      resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "수정 실패");
    }
  }
}
