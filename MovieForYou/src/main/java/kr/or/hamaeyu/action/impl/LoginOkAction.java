package kr.or.hamaeyu.action.impl;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.dto.LoginRequest;
import kr.or.hamaeyu.dto.LoginResponse;
import kr.or.hamaeyu.exception.AuthenticationException;
import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.service.LoginService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginOkAction implements Action{
	
	private final LoginService loginSvc;
	
	public LoginOkAction(){
		loginSvc = LoginService.getInstance();
	}
	
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		// 요청 파라미터
		String email = request.getParameter("email");
		String password = request.getParameter("password");
		
		ActionForward forward = new ActionForward();
		
		try {
			LoginResponse dto = loginSvc.login(LoginRequest.builder().email(email).password(password).build());
			
			//로그인 성공
			//현재 요청의 HttpSession 객체를 가져와서 저장함 - 식별자, 닉네임, 권한정보
			request.getSession().setAttribute("loginUserId", dto.getId());
			request.getSession().setAttribute("loginUserNickname", dto.getNickname());
			request.getSession().setAttribute("loginUserRole", dto.getRole());
			
			forward.setRedirect(true); // 리다이렉트 여부 true
			forward.setPath(request.getContextPath()); // 메인으로 리다이렉트
			
		}catch(AuthenticationException e) {
			log.warn("[로그인 실패] LoginAction.execute() : {}", e.getMessage());
			request.setAttribute("errorMsg", "이메일 또는 비밀번호가 올바르지 않습니다.");
			 // 로그인 화면으로 forward
		    forward.setRedirect(false);
		    forward.setPath("/WEB-INF/views/login.jsp");

		}catch (DataAccessException e) {
		    // DB 접근 실패
		    request.setAttribute("errorMsg", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
		    forward.setRedirect(false);
		    forward.setPath("/WEB-INF/views/login.jsp");
		    log.error("DB 예외 발생", e); // 실제 예외 내용은 로그에만 기록
		}
		
		return forward;
	}

}
