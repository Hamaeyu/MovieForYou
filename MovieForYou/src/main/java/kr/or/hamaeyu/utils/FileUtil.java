package kr.or.hamaeyu.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import jakarta.servlet.http.Part;
import lombok.extern.slf4j.Slf4j;

/**
 * 서블릿 Part객체를 java.io.File로 변환하는 유틸 클래스
 * 오브젝트 스토리 업로드 처리에 필요함
 * Part → File → Object Storage 업로드
 * InputStream을 여러 번 읽어야해서 필요
 * (InputStream은 한 번 읽으면 끝이므로 File로 변환하면 여러 번 읽을 수 있음)
 * 임시 디렉토리에 파일 생성 후 사용하고
 * 업로드 완료 후 삭제
 * 
 * 사용 예시:
 * File tempFile = FileUtil.convertPartToFile(part);// Part -> File 변환
 * String parUrl = ObjectStorageUtil.uploadFileAndGetParUrl(tempFile, part.getSubmittedFileName(), 1);
 * tempFile.delete(); // 업로드 후 임시 파일 삭제 
 * 
 * 사용자가 브라우저에서 이미지를 업로드(multipart/form-data)
 * 서버 서블릿이 Part객체로 요청을 받음
 * 서버는 파일 데이터를 임시 메모리(또는 임시 디스크)에 저장
 * 작은 파일은 메모리에
 * 큰 파일은 임시 디스크에 저장될 수 있다.
 * Part객체가 생성되고, 파일 데이터를 읽을 수 있는 InputStream제공
 * Part안에 InputStream형태로 데이터가 존재.
 * 
 *  왜 스트림으로 제공하는지?
 *  파일 크기가 클 수 있어서 메모리 부담을 최소화 하기 위해서,
 *  순차적으로 읽어서 파일로 저장하거나 처리 할 수 있다
 *  
 *  즉, Part객체는 서버 메모리에 전체 파일이 올라오는 게 아니라 스트림으로 읽을 수 있는 상태
 *  사용자가 업로드한 파일은 Part 내부의 InputStream 형태로 임시 메모리/디스크에 존재.
 *  한 번 InputStream을 읽으면 스트림은 소진된다. 다시 읽으려면 복사 필요함
 *  서버는 이 InputStream을 읽어 File로 저장하거나, Object Storage로 바로 업로드하는 식으로 처리
 */
@Slf4j
public class FileUtil {
	
	/**
	 * new 없이 전역에서 사용 가능하도록 static 메서드로 만듬
	 * 서블릿에서 넘어온 Part 객체(InputStream)를 디스크 상의 임시 파일로 저장
     * @param part 서블릿에서 업로드된 파일 Part 객체
     * @return 변환된 임시 File 객체
     * 	 - 실제 디스크에 생성되며, 다른 API에서 File로 읽어 사용할 수 있음
     * @throws IOException 파일 생성/쓰기 실패 시
	 */
	public static File convertPartToFile(Part part) throws IOException{
		log.debug("convertPartToFile(part={})",part);
		if(part == null) {
			log.warn("[예외] Part객체 null");
			throw new IllegalArgumentException("Part객체가 null");
			//메서드에 전달된 아규먼트가 올바르지 않을 때 발생하는 런타임 예외
		}
		
		//원본 파일 이름 가져오기
		String originalFileName = part.getSubmittedFileName();// 업로드된 파일 이름
		if(originalFileName == null || originalFileName.isEmpty()) {
			//이름을 가져오지 못하면 IOExtemption발생
			log.warn("[예외] 원본 파일 이름 없음 : {}", originalFileName);
			throw new IOException("파일 이름을 가져올 수 없습니다.");
			//입출력 처리 중 발생하는 예외
		}
		
		//임시 파일 생성 정보 설정
		String prefix = "upload_"; //파일 이름 앞 접두사
		//확장자 추출. 없으면 null
		String suffix = originalFileName.contains(".") //파일 이름에 .이 포함되어 있는지 확인.
				? originalFileName.substring(originalFileName.lastIndexOf(".")) //있으면 확장자만 가져옴
		                : null; //없으면 null저장(확장자가 없는 파일)
		// 파일 업로드 시 원래 파일 확장자를 유지하며 저장하기 위해서.
		
		//JVM 기본 임시 디렉토리에 임시 파일 생성
		File tempFile = File.createTempFile(prefix, suffix);
		
		//Part(InputStream) -> File(FileOutPutStream)쓰기
		//Part객체는 서블릿 3.0이상에서 제공하는 파일 업로드 객체.
		// 사용자가 업로드한 파일 정보를 담고 있다.
		// InputStream을 제공함 -> 실제 파일 데이터를 인풋스트림으로 읽을 수 있다.
		// 메타 정보 메서드도 있음
		// 서버에 바로 파일이 저장되지 않고, 메모리나 임시 저장소에 있다.
		// 스트림으로 읽어야함.
		try(InputStream in = new BufferedInputStream(part.getInputStream());
			     OutputStream out = new BufferedOutputStream(new FileOutputStream(tempFile));){
			
			//데이터를 읽을 때 한 번에 읽는 바이트 단위 버퍼 지정
			byte[] buffer = new byte[8192]; // 8KB 버퍼 (필요 시 16~32KB로 조정 가능)
	        int bytesRead;
	        while ((bytesRead = in.read(buffer)) != -1) {
	            out.write(buffer, 0, bytesRead);
	        }

	        // flush()는 BufferedOutputStream이 close()될 때 자동 호출됨
	        // 명시적으로 flush()를 호출해도 안전
	        out.flush();
			
			
		}catch(IOException e) {
	        log.error("[FileUtil] Part -> File 변환 실패: {}", e.getMessage(), e);
	        throw e;
	    }

	    log.debug("[FileUtil] 임시 파일 생성 완료 (Buffered): {}", tempFile.getAbsolutePath());
	    return tempFile;
	}
	
	 /**
     * 임시 파일 삭제 (안정적으로 삭제 시도)
     * 
     * @param file 삭제할 File 객체
     */
    public static void deleteTempFile(File file) {
        if (file != null && file.exists()) {
            if (file.delete()) {
                log.debug("[FileUtil] 임시 파일 삭제 완료: {}", file.getAbsolutePath());
            } else {
                log.warn("[FileUtil] 임시 파일 삭제 실패, JVM 종료 시 삭제 예약: {}", file.getAbsolutePath());
                file.deleteOnExit(); // 삭제 실패하면 JVM 종료 시 자동 삭제
            }
        }
    }

}
