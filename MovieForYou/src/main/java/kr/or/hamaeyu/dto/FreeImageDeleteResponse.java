package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class FreeImageDeleteResponse {
	private boolean success;// true: 삭제 성공, false: 실패
	private String message;// 응답으로 보낼 메세지
	private int status;   // HTTP 상태 코드 (예: 200, 400, 404, 500)
}
