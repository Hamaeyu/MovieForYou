package kr.or.hamaeyu.service;

import java.io.File;
import java.net.URLEncoder;

import org.apache.tomcat.jakartaee.commons.io.FilenameUtils;

import jakarta.servlet.http.Part;
import kr.or.hamaeyu.dao.FreeBoardDao;
import kr.or.hamaeyu.dao.FreeBoardPostImageDao;
import kr.or.hamaeyu.dao.FreeBoardTempPostImagesDao;
import kr.or.hamaeyu.dto.FreeBoardRequest;
import kr.or.hamaeyu.dto.FreeImageUploadResponse;
import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.exception.ObjectStorageException;
import kr.or.hamaeyu.model.TempPostImage;
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
	
	/*
	public void create(FreeBoardRequest dto) {
		log.debug("create(dto={})", dto);
		
	}
	*/
	
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
