package kr.or.hamaeyu.action.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;

/**
 * 로그인 페이지 화면만 보여주는 용도
 */
public class LoginAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		
		return new ActionForward(false,"/WEB-INF/views/login.jsp"); // forward;
	}

}
