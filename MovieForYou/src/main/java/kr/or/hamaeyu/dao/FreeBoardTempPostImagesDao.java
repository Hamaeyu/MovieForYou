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
	
	//비동기로 실행되는 쿼리
	private static final String SQL_INSERT_TEMP_IMAGE = 
			"insert into temp_post_image(id, temp_uuid, image_url) "
			+ "values(?, ?, ?)";
	
	// 비동기로 실행되는 쿼리
	private static final String SQL_SELECT_SEQ_NEXTVAL = "select temp_post_image_seq.nextval from dual";
	
	private static final String SQL_SELECT_SEQ_CURRVAL = "select temp_post_image_seq.currval from dual";
	
	//id로 임시 이미지 조회 -> 오브젝트 스토리지 삭제에 쓰임
	private static final String SQL_SELECT_TEMP_IMAGE_BY_ID = 
			"select id, image_url from temp_post_image where id = ?";
	
	//id로 임시 이미지 삭제 -> 오브젝트 스토리지 삭제에 쓰임
	private static final String SQL_DELETE_TEMP_IMAGE_BY_ID = 
			"delete from temp_post_image where id = ?";
	
	//썸네일 여부 설정
	private static final String SQL_UPDATE_IS_THUMBNAIL_BY_ID = 
			"update temp_post_image set is_thumbnail = ? where id = ?";
	
	//temp_uuid 기준 임시 이미지 삭제 쿼리 - 마이그레이션 완료 시 삭제시킴(서비스 계층에서 호출해서 트랜잭션으로 처리함)
	private static final String SQL_DELETE_TEMP_IMAGE = 
			"delete from temp_post_image where temp_uuid = ?";
	
	//실제 게시글에 연결(post_image)하기 위한 조회 쿼리
	//ORDER BY uploaded_at ASC필요없음 -> 괜히 성능만 저하됨 
	// 썸네일은 is_thumbnail있고, 이미지 랜더링도 post_content컬럼에서 
	// <img>태그로 되기 때문에 마이그레이션 순서 중요치 않고 판단함.
	// 근데 uploaded_at까지 select에 포함시켜서 마이그레이션 할것인가 말것인가.. 고민..
	//-> 일단 안하기로 결정. 실제 기능에는 필요없어서 불필요 작업이 될 수 있다..
	private static final String SQL_SELECT_TEMP_BY_UUID = 
			"select id, temp_uuid, image_url, is_thumbnail "
			+ "from temp_post_image "
			+ "where temp_uuid = ?";
	
	
	//배치용 : 오브젝트 스토리에서도 삭제해야 해서, 삭제 전에 찾아야함(트랜젝션 필수)
	private static final String SQL_SELECT_OLD_IMAGE_URL = 
			"select image_url from temp_post_image "
			+ "where uploaded_at < (systimestamp - interval '2' hour)";
	
	// 배치용, 삭제 조건 : 현재 시각에서 2시간 이상 지난 임시 이미지 데이터(행) 삭제함(트랜젝션 필수)
	// uploaded_at 컬럼에 인덱스 적용해둠
	// interval 오라클에서는 이부분 ? 바인딩 파라미터로 못받는다고 함
	// 시간 하드코딩 부분은 상수화로 변경도 가능 (systimestamp - interval '" + DELETE_INTERVAL_HOURS + "' hour)
	private static final String SQL_DELETE_OLE_TEMP_IMAGE = 
			"delete from temp_post_image "
			+ "where uploaded_at < (systimestamp - interval '2' hour)";
	
	private static Long getNextTempImageId() {
		Long newId = 0L;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_SEQ_NEXTVAL);
				ResultSet rs = pstmt.executeQuery();){
			if(rs.next()) {
				newId = rs.getLong(1);
			}
		}catch(SQLException e) {
			log.error("[DB 예외] {}", e.getMessage());
			throw new DataAccessException("임시 이미지 테이블의 다음 시퀀스 조회가 실패했습니다.");
		}
		return newId;
	}

	private static Long getCurrvalTempImageId() {
		Long id = 0L;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_SEQ_CURRVAL);
				ResultSet rs = pstmt.executeQuery();){
			if(rs.next()) {
				id = rs.getLong(1);
			}
		}catch(SQLException e) {
			log.error("[DB 예외] {}", e.getMessage());
			throw new DataAccessException("임시 이미지 테이블의 현재 시퀀스 조회가 실패했습니다.");
		}
		return id;
	}
	
	/**
	 * 사용자가 에디터에서 이미지 업로드 시 비동기로 처리하기 위해
	 * 임시 테이블에 insert
	 * @param tempPostImage - 업로드 된 이미지 정보를 담은 객체
	 * @return insert된 temp_post_image.id(PK)
	 * 예외 발생 시 커스텀 예외 던짐(new DataAccessException)
	 */
	public Long insertTempImage(TempPostImage tempPostImage) {
		int result = 0;
		Long id = getNextTempImageId();
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_INSERT_TEMP_IMAGE);){
			
			pstmt.setLong(1, id);
			pstmt.setString(2, tempPostImage.getTempUuid());
			pstmt.setString(3, tempPostImage.getImageUrl());
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
			log.error("[DB 예외] insert 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("DB 자유 게시판 이미지 임시 테이블 insert 실패", e);
		}
		return id;
	}
	
	/**
	 * 임시 이미지 조회
	 * @param id 삭제 대상 임시 이미지 PK
	 * @return 조회 결과(없으면 null)
	 */
	public TempPostImage selectTempImageById (Long id) {
		TempPostImage temp = null;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_TEMP_IMAGE_BY_ID);){
			pstmt.setLong(1, id);
			try(ResultSet rs = pstmt.executeQuery();) {
				if(rs.next()) {
					temp = TempPostImage.builder()
					.id(rs.getLong("id"))
					.imageUrl(rs.getString("image_url"))
					.build();
					log.info("[DB] select 성공 : {}", temp);
				}else {
					//조회가 안되면 안되는 상황
					log.warn("[DB] 조회된 결과가 없습니다. id={}", id);
				}
				
			} catch (SQLException e) {
				log.error("[DB 예외] select 쿼리 실행 실패 : {}", e.getMessage(), e);
				//SQLException은 체크 예외라서 매번 try-catch 해야 함
				//커스텀 예외로 포장하면 상위 계층에서는 catch 한 번으로 통일 처리 가능
				//서비스 건너 뛰고 컨트롤러에서 잡아서 프론트에 예외 응답으로 보내면 됨
				throw new DataAccessException("DB 자유게시판 임시 이미지 select 실패", e);
			}
		}catch(SQLException e) {
			log.error("[DB 예외] select 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("DB 자유게시판 임시 이미지 select 실패", e);
		}
		
		return temp;
	}

	
	/**
	 * 임시 이미지 테이블 행 삭제 메서드
	 * @param id where조건에 들어갈 id(pk)
	 * @return 성공 여부
	 * SQLException 발생 시 DataAccessException으로 래핑 후 던져
	 *    - 컨트롤러에서 잡아 프론트에 에러 응답 처리
	 */
	public boolean deleteTempImageById(long id) {
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_TEMP_IMAGE_BY_ID)){
			pstmt.setLong(1, id);
			int resultRow = pstmt.executeUpdate();
			if(resultRow > 0) {
				log.info("[DB] delete 성공 건 수 : {}", resultRow);
				return true;
			}else {
				log.warn("[DB] 삭제된 행이 없습니다.");
			}
		}catch(SQLException e) {
			log.error("[DB 예외] 임시이미지 테이블 delete 실패", e.getMessage(), e);
			throw new DataAccessException("DB 임시이미지 테이블 delete 실패", e );
			// 컨트롤러에서 잡아서 프론트에 예외 응답으로 보내면 됨
		}
		return false;
	}
	
	//썸네일 설정 
	public Long updateThumbnail(Connection conn, Long tempImageId) {
		try(PreparedStatement pstmt = conn.prepareStatement(SQL_UPDATE_IS_THUMBNAIL_BY_ID);){
			pstmt.setString(1, "Y");
			pstmt.setLong(2, tempImageId);
			int resultRow = pstmt.executeUpdate();
			if(resultRow > 0) {
				log.info("썸네일 update 성공 : {}", resultRow);
			}else {
				log.info("썸네일로 설정된 행이 없습니다.");
			}
		}catch(SQLException e) {
			log.error("[DB 예외] update 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("[update] 썸네일 설정 실패", e);
		}
		return tempImageId; // 썸네일로 설정한 pk
	}
	
	/**
	 * 배치용 : 일정 시간(2시간 이상) 지난 임시 이미지 행 데이터 정리용
	 * @param conn - 트랜젝션 처리에 필요
	 * @return 삭제된 행 수
	 */
	public int deleteOldTempImages(Connection conn) {
		int result = 0;
		try(PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_OLE_TEMP_IMAGE);){
			result = pstmt.executeUpdate(); //DB에서 쿼리 실행
			if(result > 0) {
				log.debug("[DB] delete 성공 건 수 {}", result);
			}
		}catch(SQLException e) {
			log.error("[DB예외] delete 실패 : {}" , e.getMessage(), e);
			throw new DataAccessException("DB 2시간 이상 지난 임시 이미지 정리 실패", e);
		}
		
		return result;
	}
	
	/**
	 * 배치용 : 일정 시간(2시간 이상) 지난 임시 이미지 url 조회함
	 * 오브젝트 스토리지 삭제에 이용함(트랜잭션 필수)
	 * @param conn - 트랜젝션 처리에 필요
	 * @return 조회된 오브젝트 스토리지 url
	 */
	public List<String> selectOldTempImageUrl(Connection conn) {
		List<String> imageUrlList = new ArrayList<String>();
		try(PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_OLD_IMAGE_URL);
				ResultSet rs = pstmt.executeQuery();) {
			
			while (rs.next()) {
				imageUrlList.add(rs.getString("image_url"));
			}
			
		} catch(SQLException e) {
			log.error("[DB 예외] select 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("DB 2시간 이상 지난 임시 이미지 url 조회 실패", e);
		}
		
		if (imageUrlList.isEmpty()) {
	        log.debug("[DB] select 조회된 행이 없습니다.");
	    }
		
		return imageUrlList;
	}
	
	/**
	 * temp_uuid 기준 임시 이미지 삭제함
	 * 서비스계층에서 호출해서
	 * 마이그레이션 끝나면 임시테이블에서 삭제 용도(트랜젝션 처리함)
	 * -> 병렬 에러 문제로 따로 함
	 * @param conn DB Connection(트랜잭션 처리하려면 파라미터로 받아서 같은 커넥션에서 해야함)
	 * @param tempUuid 삭제 조건에 들어감
	 * @return delete 성공 건 수(행 단위)
	 */
	public int deleteByUuid(String tempUuid) {
		int result = 0;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(SQL_DELETE_TEMP_IMAGE);){
			pstmt.setString(1, tempUuid);
			result = pstmt.executeUpdate();//쿼리 실행
			
			if(result > 0) {				
				log.debug("[DB] 임시 테이블 delete 성공 건 수 : {}", result);
			} else {
				log.debug("[DB] 임시 테이블 삭제된 행이 없습니다.");
			}
			
		} catch(SQLException e) {
			log.error("[DB 예외] 임시 테이블 delete 쿼리 실패", e.getMessage(), e);
			throw new DataAccessException("temp_uuid로 임시 이미지 삭제 실패", e);
		}
		
		return result;
	}
	
    /**
     * 임시 이미지 UUID 기준 조회
     * 서비스 계층에서 호출해서 트랜잭션 처리함 
     * select 해와서 post_image 테이블에 insert 
     * @param conn DB Connection
     * @param 조회 조건에 쓰이는 tempUuid
     * @return List<TempPostImage> 마이그레이션 처리할 임시이미지 데이터들
     * @throws SQLException
     */
	public List<TempPostImage> findByUuid(Connection conn, String tempUuid){
		List<TempPostImage> tempList = new ArrayList<TempPostImage>(); 
		
		try(PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_TEMP_BY_UUID);){
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
				log.error("[DB 예외] 쿼리 실행 단계에서 예외 발생", e);
				throw new DataAccessException("임시 이미지 테이블 temp_uuid로 쿼리 select 실패", e);
			}
						
		} catch(SQLException e) {
			log.error("[DB 예외] select 실패", e);
			throw new DataAccessException("임시 이미지 테이블 temp_uuid로 쿼리 select 실패", e);
		}
		
		if (tempList.isEmpty()) {
	        log.debug("[DB] select 조회된 행이 없습니다.");
	    }
		
		return tempList;
	} 
	
	
}
