package kr.or.hamaeyu.controller;

import java.io.*;
import java.util.List;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminCinemaDao;
import kr.or.hamaeyu.dto.CinemaFullInfo;

@WebServlet("/admin")
public class AdminServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	AdminCinemaDao dao = new AdminCinemaDao();
    	
    	List<CinemaFullInfo> list = dao.getCinemaList();
        request.setAttribute("cinemaList", list);

        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/admin.jsp");
        rd.forward(request, response);
    }
}