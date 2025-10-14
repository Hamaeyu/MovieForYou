package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.PostImage;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 자유 게시판에서 사용
 * CKEditor에서 업로드 된 이미지 정보 처리
 * 사용자가 작성 완료 클릭 시 
 * temp_post_image -> post_image
 * post_image로 게시글 id 연결 마이그레이션 
 */
@Slf4j
public class FreeBoardPostImageDao {
	private static final FreeBoardPostImageDao instance;
	static {
		instance = new FreeBoardPostImageDao();
	}
	private FreeBoardPostImageDao() {}
	public static FreeBoardPostImageDao getInstance() {
		return instance;
	}
	
	private static final String SQL_INSERT_POST_IMAGE = 
			"insert into post_image(post_id, image_url, is_thumbnail) "
			+ "values(?, ?, ?)";
	//uploaded_at는 기본값 + not null 설정으로 생략함
	
	private static final String SQL_SELECT_POST_IMAGE  = 
			"";
	
	/**
	 * 이미지 정보 등록
	 * @param postImage - 게시글 이미지 모델 객체
	 * @return insert된 행 수(정상 : 1)
	 */
	public int insertPostImage(PostImage postImage) {
		int result = 0;
		
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_POST_IMAGE);){
			//? 파라미터 채움(바인드 변수)
			pstmt.setLong(1, postImage.getPostId());
			pstmt.setString(2, postImage.getImageUrl());
			pstmt.setString(3, String.valueOf(postImage.getIsThumbnail()));
			result = pstmt.executeUpdate();//쿼리실행
			
			if(result > 0) {
				log.debug("[DB] insert 성공 건 수 : {}", result);
			}
			
		}catch(SQLException e) {
			log.warn("[DB 예외] 게시판 이미지 insert 실패 : {}", e.getMessage());
			throw new DataAccessException("DB post_image테이블에 insert 실패", e);
		}
		
		return result;
	}
	
	
}
