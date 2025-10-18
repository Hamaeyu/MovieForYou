package kr.or.hamaeyu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode @ToString
public class FreePostResponse {
	private boolean success; // 성공 여부
	private String message; // 실패 시 메시지
	private int status;   // HTTP 상태 코드 (예: 200, 400, 404, 500)
	//private Long postId; // 생성된 글 ID (옵션)
}
