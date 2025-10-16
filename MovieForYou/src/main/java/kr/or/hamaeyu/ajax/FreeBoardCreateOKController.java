package kr.or.hamaeyu.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dto.FreeImageDeleteResponse;
import kr.or.hamaeyu.dto.FreeImageUploadResponse;
import kr.or.hamaeyu.dto.FreePostRequest;
import kr.or.hamaeyu.dto.FreePostResponse;
import kr.or.hamaeyu.service.FreeBoardService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.stream.Collectors;

import com.google.gson.Gson;


@WebServlet("/createok.free")
@Slf4j
public class FreeBoardCreateOKController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final FreeBoardService freeSvc;
    public FreeBoardCreateOKController() {
        super();
        freeSvc = FreeBoardService.getInstance();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("자유 게시판 글 등록 비동기 호출");
		response.setContentType("application/json;charset=UTF-8");
		//클라이언트에서 JSON 문자열 읽기
		Gson gson = new Gson();
		try {
			String jsonStr = request.getReader().lines().collect(Collectors.joining());
			 // Gson으로 JSON → DTO 변환
            FreePostRequest postRequest = gson.fromJson(jsonStr, FreePostRequest.class);
            log.debug("postRequest : {}", postRequest);
            
            //세션에서 로그인 회원번호 가져오기
            Long userId = (Long) request.getSession().getAttribute("loginUserId");
            //세션에 회원번호 없는 경우
            if (userId == null) {
                writeResponse(response, false, "로그인이 필요합니다.", 401);
                return;
            }
            
            //서비스 호출
            freeSvc.create(postRequest, userId, 3); //자유게시판 타입 3번 고정
            //성공 응답
            writeResponse(response, true, "글쓰기 성공", 201);
			
		} catch (Exception e) {
			
		}
	}
	
	// 반복되는 응답 DTO 작성 메서드
	private void writeResponse(HttpServletResponse response, boolean success, String message, int status) throws IOException {
		FreePostResponse dto = FreePostResponse
				.builder()
				.success(success)
				.message(message)
				.status(status)
				.build();
		response.getWriter().write(new Gson().toJson(dto));
	}

}
