package kr.or.hamaeyu.controller;

import jakarta.servlet.*;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import kr.or.hamaeyu.dao.CinemaReviewDAO;
import kr.or.hamaeyu.dto.CinemaReviewVO;
import kr.or.hamaeyu.model.CinemaReviewPage;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@WebServlet("*.cinema")
@MultipartConfig(maxFileSize = 1024 * 1024 * 10)
public class FrontCinemaReviewController extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private final CinemaReviewDAO dao = new CinemaReviewDAO();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doProcess(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doProcess(req, res);
    }

    private void doProcess(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String uri = req.getRequestURI();
        String command = uri.substring(uri.lastIndexOf("/"));

        switch (command) {

            /* 1️⃣ 목록 페이지 */
            case "/list.cinema" -> {
                String q = opt(req.getParameter("q"));
                String field = opt(req.getParameter("field"));
                int pageNum = num(req.getParameter("page"), 1);
                int size = 10;

                int total = dao.count(q, field);
                CinemaReviewPage page = new CinemaReviewPage(pageNum, size, total);

                req.setAttribute("list", dao.findAll(q, field, page));
                req.setAttribute("page", page);
                req.setAttribute("q", q);
                req.setAttribute("field", field);

                forward(req, res, "/WEB-INF/views/cinemaReviewList.jsp");
            }

            /* 2️⃣ 작성 페이지 */
            case "/write.cinema" -> forward(req, res, "/WEB-INF/views/cinemaReviewWrite.jsp");

            /* 3️⃣ 상세 페이지 */
            case "/detail.cinema" -> {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("post", dao.findById(id));
                forward(req, res, "/WEB-INF/views/cinemaReviewDetail.jsp");
            }

            /* 4️⃣ 수정 페이지 */
            case "/edit.cinema" -> {
                long id = Long.parseLong(req.getParameter("id"));
                req.setAttribute("post", dao.findById(id));
                forward(req, res, "/WEB-INF/views/cinemaReviewEdit.jsp");
            }

            /* 5️⃣ 저장 / 수정 / 삭제 */
            case "/save.cinema" -> save(req, res);
            case "/update.cinema" -> update(req, res);
            case "/delete.cinema" -> delete(req, res);

            /* 6️⃣ 이미지 업로드 (서머노트) */
            case "/uploadImage.cinema" -> uploadImage(req, res);

            /* 7️⃣ 비동기: 브랜드 / 지역 / 상영관 */
            case "/brands.cinema" -> {
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(dao.findBrands());
            }
            case "/regions.cinema" -> {
                int brandId = Integer.parseInt(req.getParameter("brandId"));
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(dao.findRegionsByBrand(brandId));
            }
            case "/cinemas.cinema" -> {
                int brandId = Integer.parseInt(req.getParameter("brandId"));
                int regionId = Integer.parseInt(req.getParameter("regionId"));
                res.setContentType("application/json;charset=UTF-8");
                res.getWriter().write(dao.findCinemas(brandId, regionId));
            }

            default -> res.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    /* ===============================================
     * 🧩 1️⃣ 등록
     * =============================================== */
    private void save(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Long userId = (Long) req.getSession().getAttribute("loginUserId");
        if (userId == null) userId = 1L; // 테스트용

        CinemaReviewVO vo = new CinemaReviewVO();
        vo.setTitle(req.getParameter("title"));
        vo.setContent(req.getParameter("content"));
        vo.setUserId(userId);
        vo.setOverallReview(req.getParameter("overall_review"));
        vo.setCinemaRating(intOr(req.getParameter("cinema_rating")));
        vo.setSeatRating(intOr(req.getParameter("seat_rating")));
        vo.setViewTime(req.getParameter("view_time"));
        vo.setCinemaId(Long.parseLong(req.getParameter("cinema_id")));

        long newId = dao.insert(vo);
        res.sendRedirect(req.getContextPath() + "/detail.cinema?id=" + newId);
    }

    /* ===============================================
     * 🧩 2️⃣ 수정
     * =============================================== */
    private void update(HttpServletRequest req, HttpServletResponse res) throws IOException {
        CinemaReviewVO vo = new CinemaReviewVO();
        vo.setId(Long.parseLong(req.getParameter("id")));
        vo.setTitle(req.getParameter("title"));
        vo.setContent(req.getParameter("content"));
        vo.setOverallReview(req.getParameter("overall_review"));
        vo.setCinemaRating(intOr(req.getParameter("cinema_rating")));
        vo.setSeatRating(intOr(req.getParameter("seat_rating")));
        vo.setViewTime(req.getParameter("view_time"));
        vo.setCinemaId(Long.parseLong(req.getParameter("cinema_id")));

        int result = dao.update(vo);
        res.setContentType("text/html; charset=UTF-8");
        if (result > 0) {
            res.getWriter().println("<script>alert('리뷰가 수정되었습니다.'); location.href='" +
                    req.getContextPath() + "/detail.cinema?id=" + vo.getId() + "';</script>");
        } else {
            res.getWriter().println("<script>alert('수정 실패. 다시 시도해주세요.'); history.back();</script>");
        }
    }

    /* ===============================================
     * 🧩 3️⃣ 삭제
     * =============================================== */
    private void delete(HttpServletRequest req, HttpServletResponse res) throws IOException {
        long id = Long.parseLong(req.getParameter("id"));
        int result = dao.delete(id);

        res.setContentType("text/html; charset=UTF-8");
        if (result > 0) {
            res.getWriter().println("<script>alert('리뷰가 삭제되었습니다.'); location.href='" +
                    req.getContextPath() + "/list.cinema';</script>");
        } else {
            res.getWriter().println("<script>alert('삭제 실패. 다시 시도해주세요.'); history.back();</script>");
        }
    }

    /* ===============================================
     * 🧩 4️⃣ 이미지 업로드 (서머노트)
     * =============================================== */
    private void uploadImage(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
        res.setContentType("application/json;charset=UTF-8");

        Part filePart = req.getPart("file");
        if (filePart == null || filePart.getSize() == 0) {
            res.getWriter().write("{\"error\":\"no file\"}");
            return;
        }

        String uploadPath = req.getServletContext().getRealPath("/upload/cinema");
        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) uploadDir.mkdirs();

        String fileName = UUID.randomUUID() + "_" + filePart.getSubmittedFileName();
        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        String fileUrl = req.getContextPath() + "/upload/cinema/" + fileName;
        res.getWriter().write("{\"url\":\"" + fileUrl + "\"}");
    }

    /* ===============================================
     * ⚙️ 내부 유틸 메서드
     * =============================================== */
    private void forward(HttpServletRequest req, HttpServletResponse res, String path)
            throws ServletException, IOException {
        RequestDispatcher rd = req.getRequestDispatcher(path);
        rd.forward(req, res);
    }

    private String opt(String s) {
        return s == null ? "" : s.trim();
    }

    private int num(String s, int def) {
        try { return Integer.parseInt(s); } catch (Exception e) { return def; }
    }

    private Integer intOr(String s) {
        try { return Integer.valueOf(s); } catch (Exception e) { return null; }
    }
}
