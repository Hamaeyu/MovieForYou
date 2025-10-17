package kr.or.hamaeyu.dao;

import com.google.gson.Gson;
import kr.or.hamaeyu.dto.CinemaReviewVO;
import kr.or.hamaeyu.model.CinemaReviewPage;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

import java.sql.*;
import java.util.*;

public class CinemaReviewDAO {

    /* 1️⃣ 총 게시글 수 */
    public int count(String q, String field) {
        StringBuilder sql = new StringBuilder("""
            SELECT COUNT(*) 
            FROM post p 
            JOIN app_user u ON u.id = p.user_id 
            JOIN cinema_post c ON c.id = p.id 
            WHERE p.type_id = 3
        """);

        if (q != null && !q.trim().isEmpty()) {
            if ("writer".equalsIgnoreCase(field))
                sql.append(" AND LOWER(u.nickname) LIKE LOWER(?)");
            else
                sql.append(" AND LOWER(p.post_title) LIKE LOWER(?)");
        }

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            if (q != null && !q.trim().isEmpty())
                ps.setString(1, "%" + q.trim() + "%");

            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1);
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    /* 2️⃣ 목록 조회 (검색 + 페이징) */
    public List<CinemaReviewVO> findAll(String q, String field, CinemaReviewPage pg) {
        List<CinemaReviewVO> list = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
            SELECT p.id, p.post_title, TO_CHAR(p.created_at, 'YYYY-MM-DD HH24:MI') AS created_at,
                   u.nickname AS writer, c.cinema_rating, cn.cinema_name
            FROM post p 
            JOIN app_user u ON u.id = p.user_id
            JOIN cinema_post c ON c.id = p.id
            JOIN cinema cn ON cn.id = c.cinema_id
            WHERE p.type_id = 3
        """);

        List<Object> params = new ArrayList<>();
        if (q != null && !q.trim().isEmpty()) {
            if ("writer".equalsIgnoreCase(field))
                sql.append(" AND LOWER(u.nickname) LIKE LOWER(?)");
            else
                sql.append(" AND LOWER(p.post_title) LIKE LOWER(?)");
            params.add("%" + q.trim() + "%");
        }

        sql.append(" ORDER BY p.created_at DESC OFFSET ? ROWS FETCH NEXT ? ROWS ONLY");
        params.add(pg.getOffset());
        params.add(pg.getSize());

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql.toString())) {

            for (int i = 0; i < params.size(); i++)
                ps.setObject(i + 1, params.get(i));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                CinemaReviewVO vo = new CinemaReviewVO();
                vo.setId(rs.getLong("id"));
                vo.setTitle(rs.getString("post_title"));
                vo.setCreatedAt(rs.getString("created_at"));
                vo.setWriter(rs.getString("writer"));
                vo.setCinemaRating(rs.getInt("cinema_rating"));
                vo.setCinemaName(rs.getString("cinema_name"));
                list.add(vo);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return list;
    }

    /* 3️⃣ 상세 조회 */
    public CinemaReviewVO findById(long id) {
        String sql = """
            SELECT p.id, p.post_title, p.post_content,
                   TO_CHAR(p.created_at, 'YYYY-MM-DD HH24:MI') AS created_at,
                   u.nickname AS writer,
                   c.overall_review, c.cinema_rating, c.seat_rating, TO_CHAR(c.view_time, 'YYYY-MM-DD HH24:MI') AS view_time,
                   cn.cinema_name, cn.cinema_address, cb.brand_name, r.region_name, c.cinema_id
            FROM post p
            JOIN app_user u ON u.id = p.user_id
            JOIN cinema_post c ON c.id = p.id
            JOIN cinema cn ON cn.id = c.cinema_id
            JOIN cinema_brand cb ON cb.id = cn.cinema_brand_id
            JOIN region r ON r.id = cn.region_id
            WHERE p.id = ?
        """;

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                CinemaReviewVO vo = new CinemaReviewVO();
                vo.setId(rs.getLong("id"));
                vo.setTitle(rs.getString("post_title"));
                vo.setContent(rs.getString("post_content"));
                vo.setCreatedAt(rs.getString("created_at"));
                vo.setWriter(rs.getString("writer"));
                vo.setOverallReview(rs.getString("overall_review"));
                vo.setCinemaRating(rs.getInt("cinema_rating"));
                vo.setSeatRating(rs.getInt("seat_rating"));
                vo.setViewTime(rs.getString("view_time"));
                vo.setCinemaName(rs.getString("cinema_name"));
                vo.setCinemaAddress(rs.getString("cinema_address"));
                vo.setBrandName(rs.getString("brand_name"));
                vo.setRegionName(rs.getString("region_name"));
                vo.setCinemaId(rs.getLong("cinema_id"));
                return vo;
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

    /* 4️⃣ 등록 */
    public long insert(CinemaReviewVO vo) {
        String sqlPost = """
            INSERT INTO post (id, post_title, post_content, user_id, type_id, created_at)
            VALUES (cinema_post_seq.NEXTVAL, ?, ?, ?, 3, SYSDATE)
        """;
        String sqlCurr = "SELECT cinema_post_seq.CURRVAL FROM dual";
        String sqlReview = """
            INSERT INTO cinema_post (id, overall_review, cinema_rating, seat_rating, view_time, cinema_id)
            VALUES (?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI'), ?)
        """;

        try (Connection conn = ConnectionPoolHelper.getConnection()) {
            conn.setAutoCommit(false);

            try (PreparedStatement p1 = conn.prepareStatement(sqlPost);
                 PreparedStatement p2 = conn.prepareStatement(sqlCurr);
                 PreparedStatement p3 = conn.prepareStatement(sqlReview)) {

                // post
                p1.setString(1, vo.getTitle());
                p1.setString(2, vo.getContent());
                p1.setLong(3, vo.getUserId());
                p1.executeUpdate();

                // 새 ID
                ResultSet rs = p2.executeQuery();
                rs.next();
                long id = rs.getLong(1);

                // cinema_post
                p3.setLong(1, id);
                p3.setString(2, vo.getOverallReview());
                p3.setInt(3, vo.getCinemaRating());
                p3.setInt(4, vo.getSeatRating());
                p3.setString(5, vo.getViewTime());
                p3.setLong(6, vo.getCinemaId());
                p3.executeUpdate();

                conn.commit();
                return id;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            } finally {
                conn.setAutoCommit(true);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    /* 5️⃣ 수정 */
    public int update(CinemaReviewVO vo) {
        String sqlPost = "UPDATE post SET post_title=?, post_content=?, updated_at=SYSDATE WHERE id=?";
        String sqlReview = "UPDATE cinema_post SET overall_review=?, cinema_rating=?, seat_rating=?, view_time=TO_DATE(?, 'YYYY-MM-DD HH24:MI'), cinema_id=? WHERE id=?";

        try (Connection conn = ConnectionPoolHelper.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement(sqlPost);
                 PreparedStatement p2 = conn.prepareStatement(sqlReview)) {

                p1.setString(1, vo.getTitle());
                p1.setString(2, vo.getContent());
                p1.setLong(3, vo.getId());
                p1.executeUpdate();

                p2.setString(1, vo.getOverallReview());
                p2.setInt(2, vo.getCinemaRating());
                p2.setInt(3, vo.getSeatRating());
                p2.setString(4, vo.getViewTime());
                p2.setLong(5, vo.getCinemaId());
                p2.setLong(6, vo.getId());
                int result = p2.executeUpdate();

                conn.commit();
                return result;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            } finally { conn.setAutoCommit(true); }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    /* 6️⃣ 삭제 */
    public int delete(long id) {
        try (Connection conn = ConnectionPoolHelper.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement p1 = conn.prepareStatement("DELETE FROM cinema_post WHERE id=?");
                 PreparedStatement p2 = conn.prepareStatement("DELETE FROM post WHERE id=?")) {

                p1.setLong(1, id);
                p1.executeUpdate();

                p2.setLong(1, id);
                int r = p2.executeUpdate();

                conn.commit();
                return r;
            } catch (Exception e) {
                conn.rollback();
                e.printStackTrace();
            } finally { conn.setAutoCommit(true); }
        } catch (Exception e) { e.printStackTrace(); }
        return 0;
    }

    /* 7️⃣ 비동기 데이터 */
    public String findBrands() {
        return toJson("SELECT id, brand_name FROM cinema_brand ORDER BY id");
    }

    public String findRegionsByBrand(int brandId) {
        return toJson("SELECT DISTINCT r.id, r.region_name FROM region r JOIN cinema c ON c.region_id=r.id WHERE c.cinema_brand_id=? ORDER BY r.region_name", brandId);
    }

    public String findCinemas(int brandId, int regionId) {
        return toJson3("SELECT id, cinema_name, cinema_address FROM cinema WHERE cinema_brand_id=? AND region_id=? ORDER BY cinema_name", brandId, regionId);
    }

    /* 8️⃣ 평균 통계 */
    public String getStatsByCinema(long cinemaId) {
        String sql = "SELECT ROUND(AVG(cinema_rating),1) avgCinema, ROUND(AVG(seat_rating),1) avgSeat FROM cinema_post WHERE cinema_id=?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setLong(1, cinemaId);
            ResultSet rs = ps.executeQuery();
            if (rs.next())
                return String.format("{\"avgCinema\":%.1f,\"avgSeat\":%.1f}", rs.getDouble("avgCinema"), rs.getDouble("avgSeat"));
        } catch (Exception e) { e.printStackTrace(); }
        return "{\"avgCinema\":0,\"avgSeat\":0}";
    }

    /* 내부 JSON 변환 유틸 */
    private String toJson(String sql, Object... p) {
        class Row { long id; String name; Row(long i, String n){id=i; name=n;} }
        List<Row> list = new ArrayList<>();
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i=0;i<p.length;i++) ps.setObject(i+1,p[i]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Row(rs.getLong(1), rs.getString(2)));
        } catch (Exception e){ e.printStackTrace(); }
        return new Gson().toJson(list);
    }

    private String toJson3(String sql, Object... p) {
        class Row { long id; String name; String address; Row(long i,String n,String a){id=i; name=n; address=a;} }
        List<Row> list = new ArrayList<>();
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            for (int i=0;i<p.length;i++) ps.setObject(i+1,p[i]);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) list.add(new Row(rs.getLong(1), rs.getString(2), rs.getString(3)));
        } catch (Exception e){ e.printStackTrace(); }
        return new Gson().toJson(list);
    }
}
