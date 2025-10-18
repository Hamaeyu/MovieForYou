package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.or.hamaeyu.dto.FreeCommentDto;
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
	
	private static String COMMENT_SELECT_BY_POST_ID = 
			"select c.id, c.comment_content, c.created_at, c.updated_at "
			+ "u.id, u.nickname from post_comment c join app_user u "
			+ "on c.user_id = u.user_id where c.post_id = ? "
			+ "order by c.id desc "
			+ "offset ? rows fetch next ? rows only";
	
	public List<FreeCommentDto> selectCommentByPostId(Long postId, int offset, int pageSize){
		List<FreeCommentDto> list = new ArrayList<FreeCommentDto>();
		log.debug("[DB 댓글 조회] selectCommentByPostId(postId={}, offset={}, pageSize={})"
				, postId, offset, pageSize);
		try (Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(COMMENT_SELECT_BY_POST_ID);){
			pstmt.setLong(1, postId); 
			pstmt.setInt(2, offset);
			pstmt.setInt(3, pageSize);
			
			try(ResultSet rs = pstmt.executeQuery();){
				while(rs.next()) {
					list.add(FreeCommentDto.builder()
							.id(rs.getLong("id"))
							.commentContent(rs.getString("comment_content"))
							.createdAt(rs.getTimestamp("created_at").toLocalDateTime())
							.updatedAt(rs.getTimestamp("updated_at").toLocalDateTime())
							.userId(rs.getLong("userId"))
							.nickname(rs.getString("nickname"))
							.build());
				}
				
				if(list.isEmpty()) {
					log.info("해당 글에 조회되는 댓글이 없습니다. postId={}", postId);
				}
			}
		} catch (SQLException e) {
			log.error("[DB 예외] 댓글 조회가 실패 했습니다. {}", e.getMessage(), e);
			throw new DataAccessException("댓글 조회 실패");
		}
		return list;
	}
}
