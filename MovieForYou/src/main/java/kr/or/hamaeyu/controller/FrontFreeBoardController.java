package kr.or.hamaeyu.controller;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.action.impl.FreeCreateAction;
import kr.or.hamaeyu.action.impl.FreeDetailAction;
import kr.or.hamaeyu.action.impl.FreeListAction;
import kr.or.hamaeyu.action.impl.LoginAction;
import kr.or.hamaeyu.action.impl.LoginOkAction;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@WebServlet("*.free")
public class FrontFreeBoardController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	// 요청 URL -> Action 매핑, 싱글톤으로 한 번만 생성
    private static final Map<String, Action> actionFreeMap;
    
    static {
    	actionFreeMap = new HashMap<>();
    }
       
    public FrontFreeBoardController() {
        super();
    }
    
    @Override
    public void init() throws ServletException {
    	//Action 등록
    	actionFreeMap.put("/create.free", new FreeCreateAction());
    	actionFreeMap.put("/list.free", new FreeListAction());
    	actionFreeMap.put("/detail.free", new FreeDetailAction());
    }
    
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String requestUri = request.getRequestURI();
		String contextPath = request.getContextPath();
		String urlCommand = requestUri.substring(contextPath.length());
		
		log.debug("urlCommand : {}", urlCommand);
		
		Action action = actionFreeMap.get(urlCommand);
		
		if (action == null) {
			//등록되지 않은 URL일 경우 (404)
			log.warn("[등록된 Action 없음] : {}", urlCommand);
			response.sendError(HttpServletResponse.SC_NOT_FOUND, "요청한 경로를 찾을 수 없습니다.");
			return;
		}
			try {
				ActionForward forward = action.execute(request, response);
				
				//Action이 null을 반환했을 수 있음 (sendError로 이미 응답 끝난 상태)
				if (forward == null) {
					log.debug("ActionForward가 null이므로 추가 응답 처리 생략 (이미 응답 완료)");
					return;
				}
				
				if (forward.isRedirect()) { // 리다이렉트 여부 확인
					response.sendRedirect(forward.getPath()); // 리다이렉트 시킴
				} else {
					RequestDispatcher dis = request.getRequestDispatcher(forward.getPath()); // 포워드 시킬 뷰경로 지정
					dis.forward(request, response); // 포워드 시킴
				}
			} catch (Exception e) {
				log.error("[action 실행 중 예외 발생] : {}", e.getMessage(), e);
				// response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR); // 500
				request.setAttribute("errorMsg", "시스템 오류가 발생했습니다. 잠시 후 다시 시도해주세요.");
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "서버 오류");  //500
//				RequestDispatcher dis = request.getRequestDispatcher("/WEB-INF/views/login.jsp");
//				dis.forward(request, response); // 로그인 페이지로 포워드
			}
	}
		

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
