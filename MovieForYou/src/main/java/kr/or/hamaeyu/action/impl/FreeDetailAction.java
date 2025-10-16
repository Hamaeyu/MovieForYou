package kr.or.hamaeyu.action.impl;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.dto.PostDetailDto;
import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.service.FreeBoardService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class FreeDetailAction  implements Action {
	private final FreeBoardService freeSvc;
	
	public FreeDetailAction(){
		freeSvc = FreeBoardService.getInstance();
	}

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		String idStr = request.getParameter("id");//쿼리스트링에서 post.id값 가져옴
		if(idStr == null || idStr.isEmpty()) {
			 log.warn("postId가 전달되지 않음");
		     response.sendError(HttpServletResponse.SC_BAD_REQUEST, "postId가 필요합니다."); // 400
		     return null; // 이미 sendError 했으므로 forward/redirect 불필요
        }
		
		Long postId;
        try {
            postId = Long.parseLong(idStr);
        } catch (NumberFormatException e) {
        	log.warn("잘못된 postId 형식: {}", idStr);
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "잘못된 postId 형식"); // 400
            return null; // 이미 sendError 했으므로 forward/redirect 불필요
        }
        
        PostDetailDto post = null;
        try {
             post = freeSvc.getPostDetail(postId);
            if (post == null) {
                log.warn("postId={}에 해당하는 게시물이 없음", postId);
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "게시물이 없습니다."); // 404
            }
            request.setAttribute("freeList", post);
            
        } catch (DataAccessException e) {
            log.error("게시글 조회 실패: {}", e.getMessage(), e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류"); // 500
            return null;
        }
     // 정상 조회
        return new ActionForward(false, "/WEB-INF/views/board/free/freeDetail.jsp");
	}

}
