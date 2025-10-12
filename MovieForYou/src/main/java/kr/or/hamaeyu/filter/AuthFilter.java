package kr.or.hamaeyu.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import kr.or.hamaeyu.dto.LoginResponse;
import kr.or.hamaeyu.model.Role;
import lombok.extern.slf4j.Slf4j;

/**
 * 페이지별 세션 & 권한 확인 필터
 * 로그인 안 되어 있으면 -> 로그인 페이지로 리다이렉트
 * 권한 부족 -> 접근 권한 없음 페이지(403) 리다이렉트
 
 *	정적 리소스(JS,CSS,이미지 등)는 제외 시킴 
 */
@Slf4j
public class AuthFilter extends HttpFilter{
	/*
	// implements Filter안해도 이미 HttpFilter가 상속해서 구현하고 있음
	// HttpServletRequest/HttpServletResponse를 아규먼트로 받을 수 있다.
	//implements Filter하면 ServletRequest / ServletResponse를(상위 타입)으로 받아서 필요 시 형변환 해서 써야함
	//필터를 만들 때 HttpFilter를 상속하면 HTTP 전용 요청/응답을 바로 다룰 수 있어서 더 편하다!
	
	// 로그인 없이 접근 가능한 URI목록 - Set 중복X, O(1)탐색
	private static final Set<String> WHITE_SET;
	
	// 정적 리소스 패턴 (정규식)
    private static final Pattern STATIC_PATTERN = Pattern.compile(".*(\\.css|\\.js|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.woff|\\.woff2|\\.ttf)$");
    
	static {
		//List.of() : 불변 리스트 - null안됨, 수정 불가
		WHITE_SET = Set.of(
				"/",
				"/index.jsp", //서버 초기화면
				"/login.auth", // 로그인 화면 보여주는 요청
				"/loginok.auth", // 로그인 처리 요청
				"/logoutok.auth" // 로그아웃 처리 요청
				);
	}
	
	@Override
	public void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		//세션 존재 확인
		//권한 확인
		//예외 시 redirect
		String uri = request.getRequestURI(); //요청 전체 URI
		String contextPath = request.getContextPath(); // 컨텍스트 경로
		String urlCommand = uri.substring(contextPath.length());
	
		//로그인 필요 없는 URI 또는 정적 리소스 체크
        if (isLoginNotRequired(urlCommand) || urlCommand.endsWith(".signup")) { 
            log.debug("{} 로그인 필요 없는 요청/정적 리소스", urlCommand);
            chain.doFilter(request, response);
            return;
        }
		
		HttpSession session = request.getSession(false); //현재 요청에 연결된 사용자 세션 객체를 가져옴
		// request.getSession(true) : (기본값) 세션이 있으면 반환, 없으면 **새로 생성** -> 쓰면 안됨
		// 지금은 로그인 된건지 안한건지 확인하는 거라 꼭 아규먼트로 false로 줘야함
		// 세션 없으면 null을 반환함. 있으면 세션 객체 반환
		
		//세션 체크
		if(session == null || session.getAttribute("loginUser") == null) {
			log.debug("{} 세션 없음. 로그인 페이지로 리다이렉트", urlCommand);
			response.sendRedirect(request.getContextPath() + "/login.auth"); //로그인 페이지로 리다이렉트
			return;
		}
		
		//권한 체크
		LoginResponse dto = (LoginResponse) session.getAttribute("loginUser");
		Role role = dto.getRole();
		
		//관리자 요청 주소이고 로그인 사용자가 관리자 권한이 아니면, 403
		if(urlCommand.endsWith(".admin") && role != Role.ADMIN){ 
			//".admin"으로 끝나는 모든 URI에 적용
			log.debug("{} {} 권한 없음", urlCommand, role.getRoleName());
			 response.sendRedirect(request.getContextPath() + "/403"); // 권한 에러 페이지로 리다이렉트
		     return;
		}
		
		chain.doFilter(request, response);

	}
	*/

	/**
	 * 로그인이 필요없나요? 
	 * 정적리소스 요청 + 로그인 허용 요청인지 확인하는 메서드
	 * @param urlCommand (contextPath 제외) 요청 url
	 * @return true : 필요없다 (필터 제외), false : 필요하다
	 */
	/*
	private boolean isLoginNotRequired(String urlCommand) {
		if (WHITE_SET.contains(urlCommand)) { //로그인이 필요없나요? 
            return true;//네
        }
		 // 정적 리소스
        if (STATIC_PATTERN.matcher(urlCommand).matches()) { //정적 리소스인 경우
            return true;//네
        }
		return false; //그 외 로그인이 필요!
	}
	
 
	*/
}
