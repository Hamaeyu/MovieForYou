package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

public class FreeCommentDto {
	private Long postId; //게시글 ID
	private Long commentId; //댓글ID
	private String nickname; //작성자 닉네임
	private String commentContent; // 댓글 내용
	private LocalDateTime createdAt; // 작성일시
	private LocalDateTime updatedAt; //수정일시
}
