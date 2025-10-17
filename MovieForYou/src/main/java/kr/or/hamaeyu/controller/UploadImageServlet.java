package kr.or.hamaeyu.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

@WebServlet("/uploadImage.theater")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class UploadImageServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        Part filePart = req.getPart("file");
        String fileName = UUID.randomUUID() + "_" +
                Paths.get(filePart.getSubmittedFileName()).getFileName().toString();

        String uploadPath = req.getServletContext().getRealPath("/uploads/theater/summernote/");
        new File(uploadPath).mkdirs();

        filePart.write(uploadPath + File.separator + fileName);

        String fileUrl = req.getContextPath() + "/uploads/theater/summernote/" + fileName;
        resp.setContentType("application/json;charset=UTF-8");
        resp.getWriter().write("{\"url\":\"" + fileUrl + "\"}");
    }
}
