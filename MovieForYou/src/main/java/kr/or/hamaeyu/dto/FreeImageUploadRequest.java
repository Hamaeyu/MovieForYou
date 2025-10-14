package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 에디터로 이미지 업로드 요청 처리 dto(비동기 요청) 
 */
@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class FreeImageUploadRequest {
	private Part upload; //서블릿에서는 파일을 Part로 받는다고 함
	private String tempUuid; //글 단위로 프론트에서 생성해서 요청에 넣어주는 UUID
}
