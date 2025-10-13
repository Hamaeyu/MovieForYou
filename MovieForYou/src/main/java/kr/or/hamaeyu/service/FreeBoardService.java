package kr.or.hamaeyu.service;

import kr.or.hamaeyu.dao.FreeBoardDao;
import kr.or.hamaeyu.dto.FreeBoardRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreeBoardService {
	private static final FreeBoardService instance;
	private final FreeBoardDao freeDao;
	static {
		instance = new FreeBoardService();
	}
	private FreeBoardService() {
		freeDao = FreeBoardDao.getInstance();
	}
	
	public FreeBoardService getInstance() {
		return instance;
	}
	
	public void create(FreeBoardRequest dto) {
		log.debug("create(dto={})", dto);
		
	}
	
}
