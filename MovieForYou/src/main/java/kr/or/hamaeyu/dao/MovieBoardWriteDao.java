package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.time.LocalDateTime;

import kr.or.hamaeyu.dto.MovieBoardWrite;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class MovieBoardWriteDao {
	private String reviewName;
	private LocalDateTime watchDate;
	private int rating;
	private String review;
	private String imageFile;
	
	public int insertReviewPost(MovieBoardWrite post) {
		 String sql = "INSERT INTO post (POST_TITLE, POST_CONTENT, USER_ID, TYPE_ID) "
		 		+ "VALUES (?,?,?,?)";
	        try (Connection conn = ConnectionPoolHelper.getConnection();
	             PreparedStatement pstmt = conn.prepareStatement(sql)) {

	            pstmt.setString(1, post.getReviewName());
	            pstmt.setString(2, post.getReview());
	            pstmt.setString(3, "john doe");
	            pstmt.setInt(4, 9);

	            return pstmt.executeUpdate(); // 성공 시 1 반환
	        } catch (Exception e) {
	            e.printStackTrace();
	            return 0;
	        }
	}

}
