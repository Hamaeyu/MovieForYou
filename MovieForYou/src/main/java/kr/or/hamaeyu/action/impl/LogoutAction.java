package kr.or.hamaeyu.action.impl;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import lombok.extern.slf4j.Slf4j;
@Slf4j
public class LogoutAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		//세션 가져오기
		 HttpSession session = request.getSession(false);
		 if(session != null) {//세션이 존재하면 무효화
	            session.invalidate();
	            log.info("로그아웃 성공");
	        }
		 //메인 페이지로 리다이렉트
		 ActionForward forward = new ActionForward();
	        forward.setRedirect(true);           // redirect
	        forward.setPath(request.getContextPath() + "/"); 
	        log.info("메인 페이지로 리다이렉트");
	        return forward;
	}

}
