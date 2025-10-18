package kr.or.hamaeyu.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 댓글 리스트 응답 DTO
 */
@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class FreeCommentListResponse {
	 private int status;              // HTTP 상태코드
	    private boolean success;         // 성공 여부
	    private Long postId;             // 게시글 ID
	    private int page;                // 현재 페이지
	    private int size;                // 페이지 크기
	    private int totalCount;          // 전체 댓글 수
	    private List<FreeCommentDto> comments; // 댓글 리스트
}
