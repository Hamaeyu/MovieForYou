package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.TempPostImage;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * 자유게시판
 * CKEditor에서 업로드 된 이미지 정보를 임시로 저장/조회/삭제하는 Dao클래스
 * 게시글 등록 전 이미지 정보를 별도 임시 테이블(temp_post_image)에서 관리
 * 
 * 사용자가 작성 완료 클릭 시 post_image로 게시글 id 연결 마이그레이션 
 * Service 단에서 트랜잭션 관리
 * 여러 DAO 호출을 하나의 트랜잭션으로 묶기 위해 같은 Connection을 DAO로 전달해야해서 파라미터 설정함
 */
@Slf4j
public class FreeBoardTempPostImagesDao {
	//요청마다 달라지는 상태 저장 없으므로(덮어쓰기 문제 없다) 
	// 요청마다 객체 생성되는 구조보다 싱글톤 구현이 낫겠다고 판단함
	//-> 무상태를 보장해야함(안그럼 동시성 문제)
	private static final FreeBoardTempPostImagesDao instance;
	static {
		instance = new FreeBoardTempPostImagesDao();
	}
	private FreeBoardTempPostImagesDao() {}
	public static FreeBoardTempPostImagesDao getInstance() {
		return instance;
	}
	
	private static final String SQL_INSERT_TEMP_IMAGE = 
			"insert into temp_post_image(temp_uuid, image_url, is_thumbnail) "
			+ "values(?, ?, ?)";
	
	//삭제 조건 : 현재 시각에서 1시간 이상 지난 임시 이미지 데이터(행) 삭제함
	// uploaded_at 컬럼에 인덱스 적용해둠
	private static final String SQL_OLD_DELETE_TEMP_IMAGE = 
			"delete from temp_post_image "
			+ "where uploaded_at < (systimestamp - interval '1' hour)";
	
	//실제 게시글에 연결(post_image)하기 위한 조회 쿼리
	//ORDER BY uploaded_at ASC필요없음 -> 괜히 성능만 저하됨 
	// 썸네일은 is_thumbnail있고, 이미지 랜더링도 post_content컬럼에서 
	// <img>태그로 되기 때문에 마이그레이션 순서 중요치 않고 판단함.
	// 근데 uploaded_at까지 select에 포함시켜서 마이그레이션 할것인가 말것인가.. 고민..
	//-> 일단 안하기로 결정. 실제 기능에는 필요없어서 불필요 작업이 될 수 있다..
	private static final String SQL_SELECT_TEMP_IMAGE = 
			"select id, temp_uuid, image_url, is_thumbnail"
			+ "from temp_post_image "
			+ "where temp_uuid = ?";
	
	/**
	 * 사용자가 에디터에서 이미지 업로드 시 비동기로 처리하기 위해
	 * 임시 테이블에 insert
	 * @param tempPostImage - 업로드 된 이미지 정보를 담은 객체
	 * @return insert된 행 수 (정상: 1)
	 * 예외 발생 시 커스텀 예외 던짐(new DataAccessException)
	 */
	public int insertTempImage(TempPostImage tempPostImage) {
		int result = 0;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_TEMP_IMAGE);){
			
			pstmt.setString(1, tempPostImage.getTempUuid());
			pstmt.setString(2, tempPostImage.getImageUrl());
			pstmt.setString(3,String.valueOf(tempPostImage.getIsThumbnail()));
			//setChar() 같은 메서드 없음.
			//String.valueOf(char)로 문자열로 변환해서 전달
			//select시에는 공백문제 예방 위해 trim() 후 charAt(0)
			// String.valueOf(postImage.getIsThumbnail()).trim().charAt(0)
			// uploaded_at은 자동으로(default) 현재 systemestamp로 채워짐
			
			result = pstmt.executeUpdate();
			//지금은 conn.setAutoCommit(true) 기본값으로 자동 커밋
			// 추후 트랜잭션 범위 관리 필요
			// 게시글 등록 서비스에서 conn.setAutoCommit(false);
			
			if(result > 0) {
				log.debug("[DB] insert 성공 건 수 : {}", result);
			}
		}catch(SQLException e) {
			log.warn("[DB 예외] insert 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("DB 자유 게시판 이미지 임시 테이블 insert 실패", e);
		}
		return result;
	}
	
	/**
	 * 일정 시간(1시간 이상) 지난 임시 이미지 행 데이터 정리용
	 * @return 삭제된 행 수
	 */
	public int deleteOldTempImages() {
		int result = 0;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_OLD_DELETE_TEMP_IMAGE);){
			result = pstmt.executeUpdate(); //DB에서 쿼리 실행
			if(result > 0) {
				log.debug("[DB] delete 성공 건 수 {}", result);
			}
		}catch(SQLException e) {
			log.warn("[DB예외] delete 실패 : {}" , e.getMessage(), e);
			throw new DataAccessException("DB 1시간 이상 지난 임시 이미지 정리 실패", e);
		}
		
		return result;
	}
	
	public List<TempPostImage> findByUuid(String tempUuid){
		List<TempPostImage> tempList = new ArrayList<TempPostImage>(); 
		
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_TEMP_IMAGE);){
			pstmt.setString(1, tempUuid);
			try(ResultSet rs = pstmt.executeQuery();){//쿼리 실행
				while(rs.next()){ //조회되는 행이 있으면, 커서 이동하면서 실행함
					tempList.add(TempPostImage.builder()
							.id(rs.getLong("id"))
							.tempUuid(rs.getString("temp_uuid"))
							.imageUrl(rs.getString("image_url"))
							.isThumbnail(rs.getString("is_thumbnail").trim().charAt(0)) //문자열에서 인덱스 0번 가져옴
							//check와 not null로 무결성에 문제 없어서 null체크 생략
							.build());
				};// 다음 행 없을 때까지 실행
			} catch(SQLException e) {
				log.warn("[DB 예외] 쿼리 실행 단계에서 예외 발생", e);
				throw new DataAccessException("임시 이미지 테이블 temp_uuid로 쿼리 select 실패", e);
			}
						
		} catch(SQLException e) {
			log.warn("[DB 예외] select 실패", e);
			throw new DataAccessException("임시 이미지 테이블 temp_uuid로 쿼리 select 실패", e);
		}
		
		if (tempList.isEmpty()) {
	        log.debug("[DB] select 조회된 행이 없습니다.");
	    }
		
		return tempList;
	} 
	
	
}
