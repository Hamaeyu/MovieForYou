package kr.or.hamaeyu.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import kr.or.hamaeyu.dto.FreeImageUploadRequest;
import kr.or.hamaeyu.dto.FreeImageUploadResponse;
import kr.or.hamaeyu.exception.ObjectStorageException;
import kr.or.hamaeyu.service.FreeBoardService;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.oracle.bmc.model.BmcException;

@WebServlet("/freeboard/upload-image")// CKEditor에서 비동기로 호출할 엔드포인트
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,   // 1MB 이상이면 임시파일로 처리
	    maxFileSize = 1024 * 1024 * 10,    // 단일 파일 최대 10MB
	    maxRequestSize = 1024 * 1024 * 50  // 전체 요청 최대 50MB
	) // 프론트 단에서 검사해서 파일 업로드 제한 해야함
	//서블릿에서 파일 업로드를 처리할 때 필요한 어노테이션
	//HTTP 요청이 multipart/form-data로 올 때(파일 업로드 전용) 서블릿이 쉽게 처리하도록 만들어주는 설정
	//안 붙이면 IllegalStateException 발생
@Slf4j
public class FreeBoardImageUploadController extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
	private static final FreeBoardService freeBoardSvc;
    private static final List<String> ALLOWED_EXTENSIONS;
    private static final long MAX_FILE_SIZE;
	
	static { //static이여서 init메서드로 초기화 하지 않음..
		freeBoardSvc = FreeBoardService.getInstance();
		MAX_FILE_SIZE = 10 * 1024 * 1024;
		ALLOWED_EXTENSIONS = Arrays.asList(".png", ".jpg", ".jpeg", ".gif");
	}

    public FreeBoardImageUploadController() {
        super();
    }
    
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		log.debug("비동기 호출됨");
		//인코딩 처리는 필터에서 함
		//응답 세팅 -> json으로 보냄
		response.setContentType("application/json; charset=UTF-8");
		String fileName = null;
        try {
        	// CKEditor가 요청 바디에 보낸 데이터
        	FreeImageUploadRequest requestDto = FreeImageUploadRequest.builder()
        			.upload(request.getPart("upload"))
        			.tempUuid(request.getParameter("tempUuid"))
        			.build();

        	//값 검증
            if(requestDto.getUpload() == null || requestDto.getTempUuid() == null) {
                //응답 dto -> json으로 변환해서 프론트로 보냄
            	log.warn("파일 또는 tempUuid 누락, 요청 DTO: {}", requestDto);
                writeFailResponse(response, "파일 또는 tempUuid 누락");
            	 
                return;
            }
            
            //서블릿 API Part 객체의 메서드
            //서블릿 환경에서 파일 업로드할 때 쓰는 표준 API
            fileName = requestDto.getUpload().getSubmittedFileName();
            //클라이언트(브라우저)에서 업로드한 원본 파일명을 반환
            
            Long fileSize = requestDto.getUpload().getSize();
            //업로드된 파일의 **크기(바이트 단위)**를 반환
            //사용 이유: 파일 크기 제한 체크용
            
            String contentType = requestDto.getUpload().getContentType();
            //업로드된 파일의 MIME 타입을 반환
            //사용 이유: 허용된 파일 형식 체크
            
            String lowerFileName = fileName.toLowerCase();
            boolean validExt = ALLOWED_EXTENSIONS.stream().anyMatch(lowerFileName::endsWith);
            //사용 이유:확장자 체크
            
            log.info("업로드 시도: fileName={}, size={}, contentType={}, tempUuid={}",
                    fileName, fileSize, contentType, requestDto.getTempUuid());

            // 파일 검증
            if(!contentType.startsWith("image/") || fileSize > MAX_FILE_SIZE || !validExt) {
            	log.warn("허용되지 않는 파일 형식 또는 크기 초과: {} ({}) ({})", fileName, fileSize, lowerFileName);
            	writeFailResponse(response, "허용되지 않는 파일 형식 또는 크기 초과");
            	
                return;
            }
            
            //서비스 호출 -> 서비스 계층에서 트랜젝션 처리함
            //임시 이미지 테이블에 insert + 오브젝트 스토리지에 저장
            FreeImageUploadResponse responseDto = freeBoardSvc.uploadImage(requestDto.getUpload(), requestDto.getTempUuid());
            log.info("업로드 성공 : {}", responseDto);
            response.getWriter().write(new Gson().toJson(responseDto));
            
        } catch (ObjectStorageException | BmcException e) {
            // OCI 관련 예외는 그대로 던짐
            log.error("[ObjectStorage 예외] 파일명={}, 오류={}", fileName, e.getMessage(), e);
            throw e;
        } catch (IOException e) {
            // 파일 읽기/쓰기 실패
            log.error("[IO 예외] 파일명={}, 오류={}", fileName, e.getMessage(), e);
            throw new ObjectStorageException("파일 업로드 실패: IO 오류 발생", e);
        } catch (Exception e) {
        	log.error("업로드 처리 중 예외 발생", e);
        	writeFailResponse(response, "업로드 실패");
        }
    }
	
	// 반복되는 실패 응답 DTO 작성 메서드
	private void writeFailResponse(HttpServletResponse response, String message) throws IOException {
	    FreeImageUploadResponse failDto = FreeImageUploadResponse.builder()
	            .success(false)
	            .id(null)
	            .imageUrl(null)
	            .message(message)
	            .build();
	    response.getWriter().write(new Gson().toJson(failDto));
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
