package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminCinemaDao;
import kr.or.hamaeyu.dto.Cinema;

import java.io.IOException;

@WebServlet("/cinemaInsert")
public class CinemaInsertServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CinemaInsertServlet() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}
	
	@Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        request.setCharacterEncoding("UTF-8");

        String name = request.getParameter("cinemaName");
        String address = request.getParameter("address");
        int type = (Integer.parseInt(request.getParameter("type")));
        int region = (Integer.parseInt(request.getParameter("region")));
        String lat = request.getParameter("latitude");
        String lng = request.getParameter("longitude");

        double latitude = 0.0;
        double longitude = 0.0;

        try {
            latitude = Double.parseDouble(lat);
            longitude = Double.parseDouble(lng);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        Cinema dto = new Cinema();
        dto.setName(name);
        dto.setAddress(address);
        dto.setType(type);
        dto.setLng(longitude);
        dto.setLat(latitude);
        dto.setType(type);
        dto.setRegion(region);

        AdminCinemaDao dao = new AdminCinemaDao();
        int result = dao.insertTheater(dto);

        if (result > 0) {
            response.sendRedirect("admin"); // 저장 성공 시 목록 페이지로 이동
        } else {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "영화관 등록 실패");
        }
    }

}
