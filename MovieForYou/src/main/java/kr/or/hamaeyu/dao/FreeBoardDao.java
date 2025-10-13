package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.Post;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreeBoardDao {
	private static final FreeBoardDao instance;
	static {
		instance = new FreeBoardDao();
	}
	private FreeBoardDao() {}
	
	public static FreeBoardDao getInstance() {
		return instance;
	}
	
	//SQL 상수 선언
	private static final String SQL_INSERT_FREE = 
			"insert into post(post_title, post_content, created_at, user_id, type_id) "
			+ "values(?, ?, ?, ?, ?)";
	
	public int insertFreeBoard(Post post) {
		log.debug("insertFreeBoard(post={})", post);
		int result = 0;
		//try-with-resource문법 사용 시 ()안에 선언과 동시에 초기화 해야 함
		// 자동으로 리소스를 반환(close)해준다. - finally문이 필요 없음
		try(Connection conn =ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_FREE);){
			
			pstmt.setString(1, post.getPostTitle());
			pstmt.setString(2, post.getPostContent());
			pstmt.setTimestamp(3, Timestamp.valueOf(post.getCreatedAt()));
			pstmt.setLong(4, post.getId());
			pstmt.setInt(5, post.getTypeId());
			
			result = pstmt.executeUpdate(); // 쿼리 실행
			
			if(result > 0) {
				log.debug("[insert] 성공 건 수 : {}", result);
			}
			
		}catch(SQLException e) {
			log.error("[DB 예외] {}", e.getMessage());
			throw new DataAccessException("DB insert 실패", e);
		}
		
		return result;
	}
}
