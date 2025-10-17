package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.or.hamaeyu.dto.MovieBoardWrite;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class MovieBoardWriteDao {

	public int insertReviewPost(MovieBoardWrite post, long userId) {
	    String sqlPost =
	        "INSERT INTO post (id, post_title, post_content, user_id, type_id) " +
	        "VALUES (POST_ID_SEQ.NEXTVAL, ?, ?, ?, ?)";
	    String sqlReview =
	        "INSERT INTO review_post (id, short_review, star_rating) VALUES (?, ?, ?)";

	    try (Connection conn = ConnectionPoolHelper.getConnection();
	         PreparedStatement pstmtPost = conn.prepareStatement(sqlPost, new String[] { "id" });
	         PreparedStatement pstmtReview = conn.prepareStatement(sqlReview)) {

	        conn.setAutoCommit(false);

	        pstmtPost.setString(1, post.getTitle());
	        pstmtPost.setString(2, post.getReview());
	        pstmtPost.setLong(3, userId);
	        pstmtPost.setInt(4, 1);
	        pstmtPost.executeUpdate();

	        long postId = 0;
	        try (ResultSet rs = pstmtPost.getGeneratedKeys()) {
	            if (rs.next()) {
	                postId = rs.getLong(1);
	                System.out.println("[DEBUG] 생성된 POST_ID = " + postId);
	            }
	        }

	        pstmtReview.setLong(1, postId);
	        pstmtReview.setString(2, post.getShortReview());
	        pstmtReview.setInt(3, post.getStarRating());
	        pstmtReview.executeUpdate();

	        conn.commit();
	        System.out.println("[DEBUG] Commit 완료");
	        return 1;

	    } catch (Exception e) {
	        e.printStackTrace();
	        System.err.println("[ROLLBACK] 예외 발생 → 트랜잭션 롤백됨");
	        return 0;
	    }
	}
	
	public List<MovieBoardWrite> getReviewList(int page, int pageSize) {
	    List<MovieBoardWrite> list = new ArrayList<>();
	    String sql = """
	        SELECT * FROM (
	            SELECT ROWNUM AS rnum, p.id, p.post_title, p.post_content, p.user_id, p.created_at, r.short_review, r.star_rating
	            FROM post p
	            JOIN review_post r ON p.id = r.id
	            ORDER BY p.created_at DESC
	        )
	        WHERE rnum BETWEEN ? AND ?
	    """;

	    int start = (page - 1) * pageSize + 1;
	    int end = page * pageSize;

	    try (Connection conn = ConnectionPoolHelper.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        pstmt.setInt(1, start);
	        pstmt.setInt(2, end);

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	            MovieBoardWrite review = new MovieBoardWrite();
	            review.setId(rs.getInt("id"));
	            review.setTitle(rs.getString("post_title"));
	            review.setReview(rs.getString("post_content"));
	            review.setShortReview(rs.getString("short_review"));
	            review.setStarRating(rs.getInt("star_rating"));
	            review.setUserId(rs.getLong("user_id"));
	            review.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
	            list.add(review);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

	public int getReviewCount() {
	    String sql = "SELECT COUNT(*) FROM review_post";
	    try (Connection conn = ConnectionPoolHelper.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql);
	         ResultSet rs = pstmt.executeQuery()) {

	        if (rs.next()) {
	            return rs.getInt(1);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return 0;
	}

	
	public MovieBoardWrite getReviewDetail(int id) {
        String sql = """
            SELECT p.id,
                   p.post_title,
                   p.post_content,
                   p.user_id,
                   p.created_at,
                   r.star_rating,
                   r.short_review
              FROM post p
              JOIN review_post r ON p.id = r.id
             WHERE p.id = ?
        """;

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                return MovieBoardWrite.builder()
                        .id(rs.getInt("id"))
                        .title(rs.getString("post_title"))
                        .review(rs.getString("post_content"))
                        .starRating(rs.getInt("star_rating"))
                        .shortReview(rs.getString("short_review"))
                        .userId(rs.getLong("user_id"))
                        .createdAt(rs.getTimestamp("created_at").toLocalDateTime())
                        .build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }
	
	public int updateReview(MovieBoardWrite post) {
	    String sqlPost = "UPDATE post SET post_title = ?, post_content = ? WHERE id = ?";
	    String sqlReview = "UPDATE review_post SET short_review = ?, star_rating = ? WHERE id = ?";
	    
	    Connection conn = null;
	    int result = 0;

	    try {
	        conn = ConnectionPoolHelper.getConnection();
	        conn.setAutoCommit(false);

	        try (PreparedStatement pstmt1 = conn.prepareStatement(sqlPost);
	             PreparedStatement pstmt2 = conn.prepareStatement(sqlReview)) {

	            pstmt1.setString(1, post.getTitle());
	            pstmt1.setString(2, post.getReview());
	            pstmt1.setInt(3, post.getId());

	            pstmt2.setString(1, post.getShortReview());
	            pstmt2.setInt(2, post.getStarRating());
	            pstmt2.setInt(3, post.getId());

	            int rows1 = pstmt1.executeUpdate();
	            int rows2 = pstmt2.executeUpdate();

	            if (rows1 > 0 && rows2 > 0) {
	                conn.commit();
	                result = 1;
	            } else {
	                conn.rollback();
	            }

	        } catch (Exception e) {
	            conn.rollback();
	            e.printStackTrace();
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    } finally {
	        try { if (conn != null) conn.close(); } catch (SQLException e) { e.printStackTrace(); }
	    }

	    return result;
	}
	
	public int deleteReviewPost(int id) {
	    String sqlReview = "DELETE FROM review_post WHERE id = ?";
	    String sqlPost = "DELETE FROM post WHERE id = ?";
	    int result = 0;

	    try (Connection conn = ConnectionPoolHelper.getConnection();
	         PreparedStatement pstmt1 = conn.prepareStatement(sqlReview);
	         PreparedStatement pstmt2 = conn.prepareStatement(sqlPost)) {

	        conn.setAutoCommit(false);

	        pstmt1.setInt(1, id);
	        pstmt1.executeUpdate();

	        pstmt2.setInt(1, id);
	        result = pstmt2.executeUpdate();

	        conn.commit();
	    } catch (Exception e) {
	        e.printStackTrace();
	        result = 0;
	    }

	    return result;
	}




}
