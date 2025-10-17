package kr.or.hamaeyu.service;

import kr.or.hamaeyu.dao.FreeCommentDao;
import kr.or.hamaeyu.dto.FreeCommentRequest;
import kr.or.hamaeyu.model.PostComment;

public class FreeCommentService {
	private static final FreeCommentService instance;
	private final FreeCommentDao commentDao;
	static {
		instance = new FreeCommentService();
	}
	
	private FreeCommentService() {
		commentDao = FreeCommentDao.getInstance();
	}
	
	public static FreeCommentService getInstance() {
		return instance;
	}
	
	/**
     * 댓글 생성
     * @param request 댓글 DTO
     * @return 성공 여부
     */
    public boolean createComment(FreeCommentRequest request) {
        PostComment comment = PostComment.builder()
                .commentContent(request.getCommentContent())
                .postId(request.getPostId())
                .userId(request.getUserId())
                .build();
        int result = commentDao.insertComment(comment);
        return result > 0;
    }
	
}
