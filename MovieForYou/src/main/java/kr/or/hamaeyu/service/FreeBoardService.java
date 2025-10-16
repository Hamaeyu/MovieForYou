package kr.or.hamaeyu.service;

import java.io.File;
import java.net.URLEncoder;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.tomcat.jakartaee.commons.io.FilenameUtils;

import jakarta.servlet.http.Part;
import kr.or.hamaeyu.dao.FreeBoardDao;
import kr.or.hamaeyu.dao.FreeBoardPostImageDao;
import kr.or.hamaeyu.dao.FreeBoardTempPostImagesDao;
import kr.or.hamaeyu.dto.FreeBoardRequest;
import kr.or.hamaeyu.dto.FreeImageUploadResponse;
import kr.or.hamaeyu.dto.FreePostRequest;
import kr.or.hamaeyu.dto.TempImageDto;
import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.exception.ObjectStorageException;
import kr.or.hamaeyu.model.Post;
import kr.or.hamaeyu.model.PostImage;
import kr.or.hamaeyu.model.TempPostImage;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import kr.or.hamaeyu.utils.FileUtil;
import kr.or.hamaeyu.utils.ObjectStorageUtil;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 에디터 이미지 처리..
 * DB에는 이미지 경로만 저장. 
 * 실제 이미지는 오브젝트 스토리지에 저장
 * 
 * 연관 테이블 post, post_image, temp_post_image
 */
public class FreeBoardService {
	private static final FreeBoardService instance;
	private final FreeBoardDao freeDao;
	private final FreeBoardPostImageDao imageDao;
	private final FreeBoardTempPostImagesDao tempImageDao;
	
	static {
		instance = new FreeBoardService();
	}
	private FreeBoardService() {
		freeDao = FreeBoardDao.getInstance();
		imageDao = FreeBoardPostImageDao.getInstance();
		tempImageDao = FreeBoardTempPostImagesDao.getInstance();
	}
	
	public static FreeBoardService getInstance() {
		return instance;
	}
	
	/**
	 * 자유게시판 글 등록 메서드
	 * (중요) 트랜잭션 처리함
	 * 1. 글 등록
	 * 2. 썸네일 설정(첫번째 업로드 된 이미지만)
	 * 3. 이미지 테이블 마이그레이션(temp_post_image -> post_image)
	 * 같은 테이블 병렬 처리 문제로 임시 테이블 행 삭제는 따로 함
	 * 임시 테이블 행 데이터 삭제 
	 * @param dto
	 * @param userId
	 * @param typeId
	 */
	public void create(FreePostRequest dto, Long userId, int typeId) throws Exception{
		log.debug("create(dto={}, userId={}, typeId={})", dto, userId, typeId);
		 // DB 커넥션 가져오기
		Connection conn = null;
        try {
        	conn = ConnectionPoolHelper.getConnection();
        	// 수동 커밋 모드로 변경
        	conn.setAutoCommit(false);
        	
        	Post post = Post.builder()
        			.postTitle(dto.getTitle())
        			.postContent(dto.getContent())
        			.userId(userId)
        			.typeId(typeId)
        			.build();
        	//1. 글쓰기 insert -> post.id(PK)반환
        	Long postId = freeDao.insertFreeBoard(conn, post);
        	
        	//2.임시 테이블 썸네일 설정
			List<TempImageDto> tempImageList = dto.getTempImages();

			if (tempImageList != null && !tempImageList.isEmpty()) {

				//1번째 이미지만 썸네일 Y
				TempImageDto firstImage = tempImageList.get(0);//첫 번째 임시 이미지 DTO 반환
				tempImageDao.updateThumbnail(conn, firstImage.getFileId());

				// 임시테이블 → 이미지테이블 마이그레이션
				List<TempPostImage> tempList = tempImageDao.findByUuid(conn, firstImage.getTempUuid());
				 log.debug("임시 이미지 개수: {}", tempList.size());
				for (TempPostImage temp : tempList) {
					log.debug("이미지 마이그레이션: {}", temp.getImageUrl());
					imageDao.insertPostImage(conn, PostImage.builder()
							.postId(postId)
							.imageUrl(temp.getImageUrl())
							.isThumbnail(temp.getIsThumbnail())
							.build());
				}
			}
			
			conn.commit(); // 트랜잭션 성공 시 commit
			 log.info("DB commit 완료, postId={}", postId);
			//이미지 임시테이블 삭제
			if (tempImageList != null && !tempImageList.isEmpty()) {
				tempImageDao.deleteByUuid(tempImageList.get(0).getTempUuid());
				log.info("임시 테이블 삭제 성공");
			}
			
			log.info("게시글 등록 및 이미지 마이그레이션 완료 commit 수행. postId={}", postId);
			
        } catch (Exception e) {
            try {
				conn.rollback(); //롤백수행
			} catch (SQLException e1) {
				 log.error("rollback 실패", e1);
			} 
            log.error("게시글 등록 중 예외 발생, rollback 수행", e);
            throw e; // 상위에서 예외 처리
        }finally {
        	ConnectionPoolHelper.close(conn);
		}
	}

	
	/**
	 * 게시판 이미지 업로드를 처리하는 서비스 메서드
	 * 
	 * Object Storage 업로드
	 * -> image_url -> temp_post_image insert
	 * 
	 * 트랜잭션은 데이터베이스 내부에서만 보장되는 원자적 연산 단위
	 * 오브젝트 스토리지는 트랜잭션 처리 불가능...
	 * 트랜잭션은 현재 단일 insert만 있어서 오토커밋 그대로 사용.
	 * 
	 * @param upload 업로드 시킬 이미지 파일
	 * @param tempUuid 게시글 당 기준으로 묶을 uuid
	 * @return 응답으로 보낼 dto
	 */
	public FreeImageUploadResponse uploadImage(Part upload, String tempUuid) {
		
		File tempFile = null;
		String fileName = null;
		Long id = null;
		try {
			//Part -> File변환
			tempFile = FileUtil.convertPartToFile(upload);
			fileName = upload.getSubmittedFileName();
			//오브젝트 스토리지 업로드
			String parUrl = ObjectStorageUtil.uploadFileAndGetParUrl(tempFile, fileName, 7);
			
			// null 체크: 업로드 실패 시 바로 예외
	        if (parUrl == null) {
	            log.error("[업로드 실패] ObjectStorageUtil에서 PAR URL 반환 실패, 파일명={}, tempUuid={}", fileName, tempUuid);
	            throw new ObjectStorageException("파일 업로드 실패: ObjectStorage 업로드 실패");
	        }
			
			//temp_post_image테이블에 insert
			TempPostImage temp = TempPostImage.builder()
					.tempUuid(tempUuid)
					.imageUrl(parUrl)
					.build();
			id = tempImageDao.insertTempImage(temp); //insert쿼리 호출
			
			log.info("[업로드 성공] id={}, 파일명={}, tempUuid={}, id={}, parUrl={}", id, fileName, tempUuid, id, parUrl);
			
			return FreeImageUploadResponse.builder()
			.success(true)
			.id(id)
			.imageUrl(parUrl)
			.message("오브젝트스토리지 업로드 + 임시 이미지 테이블 insert 성공")
			.build();
			
			
		}catch (DataAccessException e) {
		    log.error("[DB 예외] id={}, 파일명={}, tempUuid={}, 오류={}", id, fileName, tempUuid, e.getMessage(), e);
		    throw e;
		}catch(Exception e) {
			log.error("[업로드 예외] id={}, 파일명={}, tempUuid={}, 오류={}", id, fileName, tempUuid, e.getMessage(), e);
			throw new DataAccessException("업로드가 실패했습니다.");
		}finally {
			//임시 파일 삭제 - 메서드에서 내부에서 null 검사 후 안전하게 닫음
			FileUtil.deleteTempFile(tempFile);
		}
	}
	
	/**
	 * 오브젝트 스토리지 + 임시 이미지 삭제
	 * 오브젝트 스토리지는 트랜잭션 처리 못함(외부개념)
	 * DB 삭제 전에 Object Storage 삭제 선행 필수
	 *    - 그래야 DB와 파일 스토리지 상태 동기화
	 * @param id 삭제할 임시 이미지 테이블의 PK
	 */
	public void  deleteTempImage(Long id) throws ObjectStorageException, DataAccessException {
			//DB에서 이미지 정보 조회
			TempPostImage temp= tempImageDao.selectTempImageById(id);
			if(temp == null) {
				log.warn("삭제 대상 임시 이미지가 없음. id={}", id);
				throw new DataAccessException("삭제 대상 이미지 없음 id=" + id);
			}
			
			//오브젝트 스토리지 삭제
			ObjectStorageUtil.deleteFile(temp.getImageUrl()); //실패 시 예외 던짐
			//예외 안나면 성공
			log.info("오브젝트 스토리지 삭제 성공. id={}", id);
			
			//임시 테이블에서 삭제
			boolean isDeleted = tempImageDao.deleteTempImageById(id);
			if (!isDeleted) {
				log.error("DB 임시 이미지 삭제 실패. id={}", id);
				throw new DataAccessException("DB 삭제 실패 id=" + id);
			}
			log.info("DB 임시 이미지 삭제 성공. id={}", id);
			 
	}
	
	
}
