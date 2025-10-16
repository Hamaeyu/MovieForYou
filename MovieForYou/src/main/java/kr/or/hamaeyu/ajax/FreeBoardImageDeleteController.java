package kr.or.hamaeyu.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.dto.FreeImageDeleteRequest;
import kr.or.hamaeyu.dto.FreeImageDeleteResponse;
import kr.or.hamaeyu.dto.FreeImageUploadResponse;
import kr.or.hamaeyu.exception.ObjectStorageException;
import kr.or.hamaeyu.service.FreeBoardService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import org.json.JSONObject;

import com.google.gson.Gson;
import com.oracle.bmc.model.BmcException;

@WebServlet("/freeboard/delete-image")
@Slf4j
/**
 * CKEditor5에서 업로드한 임시 이미지 삭제용 서블릿
 * 
 * 프론트에서 id(PK : temp_post_image.id)를 받음
 * Service 호출하여 오브젝트 스토리지 + 임시테이블 DB에서 이미지 삭제
 * JSON 형식으로 결과 반환
 */
public class FreeBoardImageDeleteController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private final FreeBoardService freeSvc;
       
    public FreeBoardImageDeleteController() {
        super();
        this.freeSvc = FreeBoardService.getInstance();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("[이미지 업로드 삭제 비동기 호출]");
		response.setContentType("application/json;charset=UTF-8"); // 응답 타입 지정 - JSON이라고 브라우저에게 알려줌(UTF-8 한글처리)
		// 지정해야 프론트에서 파싱 오류가 안난다

		FreeImageDeleteResponse resDto;
		try {
			String idStr = request.getParameter("id");
			
			if(idStr == null || idStr.isEmpty()) {
				// 공부용이라 주석 때문에 메서드 호출로 안쓰고 남김
				resDto = FreeImageDeleteResponse.builder()
						.success(false)
						.message("[ERROR] id가 누락되었습니다.")
						.status(400) //Bad Request (클라이언트 요청 오류)
						.build();
				response.getWriter().write(new Gson().toJson(resDto));
				//응답 객체(HttpServletResponse) - 서블릿 컨테이너(톰캣)이 자동으로 생성해서 클라이언트에게
				// 응답으로 전달해준다
				// getWriter() : 응답 본문(body)에 텍스트를 쓸 수 있는 출력 스트림 반환(PrintWriter)
				// 문자열 데이터 쓸 수 있다(HTML, JSON, XML 등) -> 도구 획득(자동 자원 관리해서 닫으면 안된다고함)
				// .write : 문자열 형태 데이터를 실제로 쓰고 전송 // 버퍼를 통해 HTTP 응답 본문에 작성됨
				// 실제로 데이터를 클라이언트로 전달
				// [참고] 이진 데이터(이미지, 파일)은 response.getOutPutStream()을 사용해야 한다.
				// 응답을 여러 번 쓰면 IllegalStateException 발생할 수 있다
				// 스트림은 한 번만 닫히기 때문
				return; 
			}
			
			Long id = Long.parseLong(idStr);
			//서비스 호출 - 오브젝트 스토리지 + 임시 테이블 삭제
			freeSvc.deleteTempImage(id); //실패 시 서비스에서 예외 던지는 구조
			
			resDto = FreeImageDeleteResponse.builder()
					.success(true)
					.message("삭제 완료")
					.status(200)
					.build();
			response.getWriter().write(new Gson().toJson(resDto));
			
		} catch (ObjectStorageException | BmcException e) {
			log.error("[ObjectStorage 예외] {}", e.getMessage(), e);
			writeFailResponse(response, "오브젝트 스토리지 오류로 실패", 500);
		} catch (IOException e) {
			// 파일 읽기/쓰기 실패
			log.error("[IO 예외] {}", e.getMessage(), e);
			writeFailResponse(response, "파일 읽기/쓰기 실패: IO 오류 발생", 500);
		} catch (Exception e) {
			log.error("업로드 처리 중 예외 발생", e);
			writeFailResponse(response, "업로드 실패", 500);
		}
	}
	
	// 반복되는 실패 응답 DTO 작성 메서드
	// 나중에 성공/실패 공용으로 빼기
	// 좋은점 -> 응답 구조 일관성 유지, 코드 중복 제거, 실수 방지(응답 DTO를 빼먹거나 status를 안 넣는 등의 실수 방지)
	private void writeFailResponse(HttpServletResponse response, String message, int status) throws IOException {
		FreeImageDeleteResponse failDto = FreeImageDeleteResponse
				.builder()
				.success(false)
				.message(message)
				.status(status)
				.build();
		response.getWriter().write(new Gson().toJson(failDto));
	}

}
