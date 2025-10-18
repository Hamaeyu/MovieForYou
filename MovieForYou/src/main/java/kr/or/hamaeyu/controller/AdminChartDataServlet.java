package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dao.AdminChartDao;

import java.io.IOException;
import java.util.Map;

import com.google.gson.JsonObject;

@WebServlet("/admin/chartData")
public class AdminChartDataServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws IOException {
        resp.setContentType("application/json; charset=UTF-8");

        AdminChartDao dao = new AdminChartDao();
        Map<String, Integer> data = dao.getTodayPostCounts();

        JsonObject json = new JsonObject();
        json.addProperty("free", data.getOrDefault("free", 0));
        json.addProperty("movieReview", data.getOrDefault("movieReview", 0));
        json.addProperty("cinemaReview", data.getOrDefault("cinemaReview", 0));

        resp.getWriter().write(json.toString());
    }
}
