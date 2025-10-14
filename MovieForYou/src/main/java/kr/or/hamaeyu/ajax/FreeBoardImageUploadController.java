package kr.or.hamaeyu.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

import com.google.gson.JsonObject;

@WebServlet("/freeboard/upload-image")// CKEditor에서 호출할 엔드포인트
@MultipartConfig(
	    fileSizeThreshold = 1024 * 1024,   // 1MB 이상이면 임시파일로 처리
	    maxFileSize = 1024 * 1024 * 10,    // 단일 파일 최대 10MB
	    maxRequestSize = 1024 * 1024 * 50  // 전체 요청 최대 50MB
	) // 프론트 단에서 검사해서 파일 업로드 제한 해야함
//서블릿에서 파일 업로드를 처리할 때 필요한 어노테이션
//HTTP 요청이 multipart/form-data로 올 때(파일 업로드 전용) 서블릿이 쉽게 처리하도록 만들어주는 설정
//안 붙이면 IllegalStateException 발생
@Slf4j
public class FreeBoardTempPostImageController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public FreeBoardTempPostImageController() {
        super();
    }
    
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//인코딩 처리는 필터에서 함
		//응답 세팅 -> json으로 보냄
//		response.setContentType("application/json; charset=UTF-8");
//		
//		JsonObject json = new JsonObject();
//
//        try {
//            Part filePart = request.getPart("upload"); // CKEditor가 보낸 파일
//            String tempUuid = request.getParameter("tempUuid");
//
//            if(filePart == null || tempUuid == null) {
//                json.addProperty("success", false);
//                json.addProperty("message", "파일 또는 tempUuid 누락");
//                response.getWriter().write(json.toString());
//                return;
//            }
//
//            String fileName = filePart.getSubmittedFileName();
//            long fileSize = filePart.getSize();
//            String contentType = filePart.getContentType();
//
//            // 파일 검증
//            if(!contentType.startsWith("image/") || fileSize > 10 * 1024 * 1024) {
//                json.addProperty("success", false);
//                json.addProperty("message", "허용되지 않는 파일 형식 또는 크기 초과");
//                response.getWriter().write(json.toString());
//                return;
//            }
	}


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request, response);
	}

}
