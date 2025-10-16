package kr.or.hamaeyu.action.impl;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;

/**
 * 자유게시판 글쓰기 페이지 화면만 보여주는 용도
 */
public class FreeCreateAction implements Action{

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		
		return new ActionForward(false,"/WEB-INF/views/board/free/freeWrite.jsp");
		// 리다이렉트 여부 false, 포워드 시킬 뷰 경로 지정
	}

}
