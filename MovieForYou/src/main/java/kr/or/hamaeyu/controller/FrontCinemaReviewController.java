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
import java.util.*;

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

            /* 1️⃣ 목록 페이지 (목데이터 버전) */
            case "/list.cinema" -> {
                // 🎬 목데이터 생성
                List<CinemaReviewVO> dummyList = new ArrayList<>();

                CinemaReviewVO v1 = new CinemaReviewVO();
                v1.setId(1L);
                v1.setTitle("CGV 용산아이파크몰 - 사운드 최고!");
                v1.setOverallReview("IMAX관은 진짜 몰입감 미쳤어요. 음향도 완벽합니다.");
                v1.setCinemaRating(5);
                v1.setSeatRating(5);
                v1.setViewTime("2025-10-10");
                v1.setUserId(101L);
                dummyList.add(v1);

                CinemaReviewVO v2 = new CinemaReviewVO();
                v2.setId(2L);
                v2.setTitle("메가박스 강남 - 깔끔하고 조용함");
                v2.setOverallReview("시설이 깨끗하고 좌석 간격도 넓어요. 다만 팝콘이 비쌉니다.");
                v2.setCinemaRating(4);
                v2.setSeatRating(4);
                v2.setViewTime("2025-10-12");
                v2.setUserId(102L);
                dummyList.add(v2);

                CinemaReviewVO v3 = new CinemaReviewVO();
                v3.setId(3L);
                v3.setTitle("롯데시네마 수원 - 평범하지만 무난");
                v3.setOverallReview("가성비 좋은 일반관이에요. 주차장이 조금 협소해요.");
                v3.setCinemaRating(3);
                v3.setSeatRating(3);
                v3.setViewTime("2025-10-15");
                v3.setUserId(103L);
                dummyList.add(v3);

                CinemaReviewVO v4 = new CinemaReviewVO();
                v4.setId(4L);
                v4.setTitle("CGV 평택 - 주차가 편하고 쾌적함");
                v4.setOverallReview("직원분들이 친절하고 상영관도 깨끗했어요.");
                v4.setCinemaRating(4);
                v4.setSeatRating(5);
                v4.setViewTime("2025-10-17");
                v4.setUserId(104L);
                dummyList.add(v4);

                // ✅ 페이징 객체 (총 4개)
                CinemaReviewPage page = new CinemaReviewPage(1, 10, dummyList.size());

                // JSP 전달
                req.setAttribute("list", dummyList);
                req.setAttribute("page", page);
                req.setAttribute("q", "");
                req.setAttribute("field", "");

                forward(req, res, "/WEB-INF/views/cinemaReviewList.jsp");
            }

            /* 2️⃣ 작성 페이지 */
            case "/write.cinema" -> forward(req, res, "/WEB-INF/views/cinemaReviewWrite.jsp");

            /* 3️⃣ 상세 페이지 */
            case "/detail.cinema" -> {
                long id = Long.parseLong(req.getParameter("id"));

                // 기존 dummyList 재활용 (임시로 다시 생성)
                List<CinemaReviewVO> dummyList = new ArrayList<>();

                CinemaReviewVO v1 = new CinemaReviewVO();
                v1.setId(1L);
                v1.setTitle("CGV 용산아이파크몰 - 사운드 최고!");
                v1.setOverallReview("IMAX관은 진짜 몰입감 미쳤어요. 음향도 완벽합니다.");
                v1.setCinemaRating(5);
                v1.setSeatRating(5);
                v1.setViewTime("2025-10-10");
                v1.setUserId(101L);
                dummyList.add(v1);

                CinemaReviewVO v2 = new CinemaReviewVO();
                v2.setId(2L);
                v2.setTitle("메가박스 강남 - 깔끔하고 조용함");
                v2.setOverallReview("시설이 깨끗하고 좌석 간격도 넓어요. 다만 팝콘이 비쌉니다.");
                v2.setCinemaRating(4);
                v2.setSeatRating(4);
                v2.setViewTime("2025-10-12");
                v2.setUserId(102L);
                dummyList.add(v2);

                CinemaReviewVO v3 = new CinemaReviewVO();
                v3.setId(3L);
                v3.setTitle("롯데시네마 수원 - 평범하지만 무난");
                v3.setOverallReview("가성비 좋은 일반관이에요. 주차장이 조금 협소해요.");
                v3.setCinemaRating(3);
                v3.setSeatRating(3);
                v3.setViewTime("2025-10-15");
                v3.setUserId(103L);
                dummyList.add(v3);

                CinemaReviewVO v4 = new CinemaReviewVO();
                v4.setId(4L);
                v4.setTitle("CGV 평택 - 주차가 편하고 쾌적함");
                v4.setOverallReview("직원분들이 친절하고 상영관도 깨끗했어요.");
                v4.setCinemaRating(4);
                v4.setSeatRating(5);
                v4.setViewTime("2025-10-17");
                v4.setUserId(104L);
                dummyList.add(v4);

                // 요청한 ID와 일치하는 리뷰 찾기
                CinemaReviewVO found = dummyList.stream()
                        .filter(r -> r.getId() == id)
                        .findFirst()
                        .orElse(null);

                req.setAttribute("post", found);
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
