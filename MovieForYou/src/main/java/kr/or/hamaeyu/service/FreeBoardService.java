package kr.or.hamaeyu.service;

import kr.or.hamaeyu.dao.FreeBoardDao;
import kr.or.hamaeyu.dao.FreeBoardPostImageDao;
import kr.or.hamaeyu.dto.FreeBoardRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
/**
 * 구현 난이도 있음..
 * 에디터 이미지 처리..
 * 이미지 업로드 시, DB에는 이미지 경로만 저장. 이미지는 오브젝트 스토리지에 저장
 * 비동기 처리로 진행됨 + (필수)트랜잭션 처리
 * 연관 테이블 post, post_image, temp_post_image
 */
public class FreeBoardService {
	private static final FreeBoardService instance;
	private final FreeBoardDao freeDao;
	private final FreeBoardPostImageDao imageDao;
	
	static {
		instance = new FreeBoardService();
	}
	private FreeBoardService() {
		freeDao = FreeBoardDao.getInstance();
		imageDao = FreeBoardPostImageDao.getInstance();
	}
	
	public static FreeBoardService getInstance() {
		return instance;
	}
	
	public void create(FreeBoardRequest dto) {
		log.debug("create(dto={})", dto);
		
	}
	
}
