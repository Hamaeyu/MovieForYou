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
public class FreeCommentDto {
	private Long id; //댓글ID - 삭제 용도
	private Long userId; //댓글 작성 회원 ID - 로그인 사용자랑 같은지 확인해서 
	// 수정 + 삭제 용도
	private String nickname; //작성자 닉네임
	private String commentContent; // 댓글 내용
	private LocalDateTime createdAt; // 작성일시
	private LocalDateTime updatedAt; //수정일시
}
