package kr.or.hamaeyu.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.action.impl.LoginAction;
import kr.or.hamaeyu.action.impl.LoginOkAction;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet("*.auth")
public class FrontLoginController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// 요청 URL -> Action 매핑, 싱글톤으로 한 번만 생성
    private static final Map<String, Action> actionAuthMap;
    
    static {
    	actionAuthMap = new HashMap<>();
    }
    
    public FrontLoginController() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	//Action 등록
    	actionAuthMap.put("/login.auth", new LoginAction());
    	actionAuthMap.put("/loginok.auth", new LoginOkAction());
    }
    
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//한글 처리는 필터로 처리
		//url방식 사용함
		String requestUri = request.getRequestURI();//현재 요청 url를 문자열로 반환함
		String contextPath = request.getContextPath(); //컨텍스트 경로를 문자열로 반환
		String urlCommand = requestUri.substring(contextPath.length());
		//요청 url에서 컨텍스트경로 길이부터 시작해서 끝까지 반환
		// 컨텍스트 경로는 제외하고 반환한다는 뜻
		
		log.debug("urlCommand : {}", urlCommand);
		
		Action action = actionAuthMap.get(urlCommand);
		//파싱한 요청 url에 맞는 Action객체 반환
		//init메서드에서 미리 등록한 HashMap 객체에서 찾음
		//없으면 null 반환 -> 없는 URL 요청 → 404 처리 나오겠지만.. 에러 페이지 처리 해야함!!!
		if(action != null) {
			try {
				ActionForward forward = action.execute(request, response);
				
				if(forward.isRedirect()) { //리다이렉트 여부 확인
					response.sendRedirect(forward.getPath()); // 리다이렉트 시킴
				}else {
					 RequestDispatcher dis = request.getRequestDispatcher(forward.getPath()); //포워드 시킬 뷰경로 지정
					 dis.forward(request, response); // 포워드 시킴
				}
			}catch(Exception e) {
				log.error("[action 실행 중 예외 발생] : {}", e.getMessage(), e);
				//response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
				request.setAttribute("errorMsg", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
				RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
				dis.forward(request, response); // 로그인 페이지로 포워드
			} 
		}else {
			log.warn("[등록된 Action 없음] : 404");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            //응답 객체에 에러를 보냄. 404처리
            //이 요청 주소에 매핑된 Action 없다
            //TODO : 커스텀 에러 페이지 web.xml에서 설정
            return;
        }
		
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
