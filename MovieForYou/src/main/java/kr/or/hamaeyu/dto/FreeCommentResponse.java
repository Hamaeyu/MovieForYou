package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

//응답으로 보내서 insert 후 새로 등록된 댓글 1개만 append
@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class FreeCommentResponse {
	private int status; //HTTP 상태코드
	private boolean success; //성공여부
	private Long postId; //게시글 ID
	//비동기로 append()시킴
	private Long commentId; // 새로 등록된 댓글ID
	private String nickname; //작성자 닉네임
	private String commentContent; // 댓글 내용
	private LocalDateTime createdAt; // 작성일시
	private LocalDateTime updatedAt; //수정일시
}
