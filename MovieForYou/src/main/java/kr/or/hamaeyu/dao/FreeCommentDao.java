package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.PostComment;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FreeCommentDao {
	private static final FreeCommentDao instance;
	static {
		instance = new FreeCommentDao();
	}
	private FreeCommentDao() {}
	
	public static FreeCommentDao getInstance() {
		return instance;
	}
	
	private static final String FREE_INSERT_COMMENT = 
			"insert into post_comment(comment_content, post_id, user_id) values(?, ?, ?)";
	
	/**
	 * 댓글 insert 메서드
	 * @param comment 댓글 모델 객체 
	 * @return 수행 건 수
	 */
	public int insertComment(PostComment comment) {
		int resultRow = 0;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(FREE_INSERT_COMMENT)) {
			pstmt.setString(1, comment.getCommentContent());
			pstmt.setLong(2, comment.getPostId());
			pstmt.setLong(3, comment.getUserId());
			resultRow = pstmt.executeUpdate();
			
			if(resultRow > 0) {
				log.info("[DB] 댓글 insert 성공 건수 : {}", resultRow);
			}else {
				log.warn("[DB] 댓글 insert 수행된 건 수가 없습니다. resultRow : {}", resultRow);
			}
		} catch (SQLException e) {
			log.error("[DB 예외] 댓글 insert 실패 {}", e.getMessage(), e);
			throw new DataAccessException("댓글 insert 실패했습니다.");
		}
		
		return resultRow;
	}
}
