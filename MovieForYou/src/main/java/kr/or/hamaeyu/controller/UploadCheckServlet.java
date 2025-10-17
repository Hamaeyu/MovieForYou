package kr.or.hamaeyu.controller;

import java.io.File;
import java.io.IOException;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/checkUpload.cinema")
public class UploadCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String uploadPath = req.getServletContext().getRealPath("/upload/cinema");
        File dir = new File(uploadPath);
        if (!dir.exists()) dir.mkdirs();

        res.setContentType("text/plain; charset=UTF-8");
        res.getWriter().println("✅ Upload 경로 점검 완료: " + uploadPath);
    }
}
