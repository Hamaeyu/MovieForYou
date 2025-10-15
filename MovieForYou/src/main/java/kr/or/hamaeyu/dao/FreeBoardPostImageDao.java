package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

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
 * 
 * 서비스 계층에서 트랜잭션 필요한 작업은 Connection을 전달받음
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
	
	//post_image 신규 등록
	private static final String SQL_INSERT_POST_IMAGE = 
			"insert into post_image(post_id, image_url, is_thumbnail) "
			+ "values(?, ?, ?)";
	//uploaded_at는 기본값 + not null 설정으로 생략함
	
	//특정 게시글(post_id)에 속한 이미지 전체 조회
	//post테이블의 post_content에 <img>태그에서 랜더링되므로 필요없는데.. 관리용으로 만들어는 둠
	private static final String POST_IMAGE_SELECT_ALL_BY_POST_ID  = 
			"select id, post_id, image_url, is_thumbnail, uploaded_at "
			+ "from post_image "
			+ "where post_id = ?";
	
	//특정 게시글(post_id)에 속한 모든 이미지 삭제
	private static final String POST_IMAGE_DELETE_ALL_BY_POST_ID =
			"delete from post_image where post_id = ?";
	
	//게시글 수정 시 기존 이미지 1건 삭제
	private static final String POST_IMAGE_DELETE_BY_ID =
			"delete from post_image where id = ?";
	
	//썸네일 조회(단건)
	private static final String POST_IMAGE_SELECT_THUMBNAIL_IMAGE =
			"select id, post_id, image_url, is_thumbnail, uploaded_at "
			+ "from post_image "
			+ "where post_id = ? and is_thumbnail = 'Y'";
	
	//썸네일 지정 
	//현재 게시글의 이미지 중 첫 번째 이미지를 썸네일로 자동 지정(수정용)
    private static final String POST_IMAGE_UPDATE_THUMBNAIL =
    		"update post_image set is_thumbnail = 'Y' where id = ?";
    
    //썸네일 초기화 (모든 이미지 N 처리)
    private static final String POST_IMAGE_UPDATE_RESET_THUMBNAIL = 
    	    "update post_image set is_thumbnail = 'N' WHERE post_id = ?";
	
	/**
	 * 이미지 정보 등록
	 * @param conn - 트랜잭션 처리 위해 주입받아야 함
	 * @param postImage - 게시글 이미지 모델 객체
	 * @return insert된 행 수(정상 : 1)
	 */
	public int insertPostImage(Connection conn, PostImage postImage) {
		int result = 0;
		
		try(PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_POST_IMAGE);){
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
	
	 /** 게시글 이미지 삭제 (단건) 
	  * post_image테이블의 id컬럼으로 삭제함
	  * */
    public int deleteById(Connection conn, long imageId) {
        int result = 0;
    	try (PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_DELETE_BY_ID)) {
            pstmt.setLong(1, imageId);
            result = pstmt.executeUpdate();
            
            if(result > 0) {
    			log.debug("[DB] delete 성공 건 수 : {}", result);
    		}
            
        } catch(SQLException e) {
        	log.warn("[DB 예외] 게시판 이미지 delete 실패 : {}", e.getMessage());
        	throw new DataAccessException("DB post_image테이블에 delete 실패", e);
        }
    	
    	return result;
    }

    /** 게시글 이미지 전체 삭제 */
    public int deleteAllByPostId(Connection conn, long postId) {
    	int result = 0;
    	try (PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_DELETE_ALL_BY_POST_ID)) {
            pstmt.setLong(1, postId);
            result = pstmt.executeUpdate();
            
            if(result > 0) {
    			log.debug("[DB] delete 성공 건 수 : {}", result);
    		}
            
        } catch(SQLException e) {
        	log.warn("[DB 예외] 게시판 이미지 delete 실패 : {}", e.getMessage());
        	throw new DataAccessException("DB post_image테이블에 delete 실패", e);
        }
    	
    	return result;
    }

    /** 썸네일 초기화 (모든 이미지 N) - 트랜젝션 필수 */
    public int resetThumbnail(Connection conn, long postId) {
    	int result = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_UPDATE_RESET_THUMBNAIL);) {
            pstmt.setLong(1, postId);
            result = pstmt.executeUpdate();
            if(result > 0) {
    			log.debug("[DB] update 성공 건 수 : {}", result);
    		}
            
        } catch(SQLException e) {
        	log.warn("[DB 예외] 게시판 이미지 썸네일 초기화 update 실패 : {}", e.getMessage());
        	throw new DataAccessException("DB post_image테이블에 썸네일 초기화 update 실패", e);
        }
    	
    	return result;
    }

    /** 특정 이미지 썸네일 지정 - 트랜젝션 필수 */
    public int updateThumbnail(Connection conn, long imageId) {
    	int result = 0;
        try (PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_UPDATE_THUMBNAIL)) {
            pstmt.setLong(1, imageId);
            result = pstmt.executeUpdate();
            if(result > 0) {
    			log.debug("[DB] update 성공 건 수 : {}", result);
    		}
            
        } catch(SQLException e) {
        	log.warn("[DB 예외] 게시판 이미지 썸네일 지정 update 실패 : {}", e.getMessage());
        	throw new DataAccessException("DB post_image테이블에 썸네일 지정 update 실패", e);
        }
    	
    	return result;
    }

    /** 단건 썸네일 조회 */
    public PostImage selectThumbnail(Connection conn, long postId) {
    	PostImage postImage = null;
        try (PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_SELECT_THUMBNAIL_IMAGE);) {
            pstmt.setLong(1, postId);
            try(ResultSet rs = pstmt.executeQuery();){
                if(rs.next()) { //id, post_id, image_url, is_thumbnail, uploaded_at
                	postImage = PostImage.builder()
                			.id(rs.getLong("id"))
                			.postId(rs.getLong("post_id"))
                			.imageUrl(rs.getString("image_url"))
                			.isThumbnail(rs.getString("is_thumbnail").charAt(0)) //문자열로 받은 다음 인덱스 0번째로 초기화
                			.uploadedAt(rs.getTimestamp("uploaded_at").toLocalDateTime())
                			.build();
                }else {
                	log.debug("[DB] select 조회된 썸네일이 없습니다.");
                }
            }catch (SQLException e) {
            	log.warn("[DB 예외] select 쿼리 실패 : {}", e.getMessage(), e);
            	throw new DataAccessException("게시판 이미지 테이블 썸네일 조회 실패", e);
    		}
        } catch (SQLException e) {
        	log.warn("[DB 예외] select 게시판 이미지 테이블 썸네일 조회 실패 : {}", e.getMessage(), e);
        	throw new DataAccessException("게시판 이미지 테이블 썸네일 조회 실패", e);
		}
        
        return postImage;
    }
	
}
