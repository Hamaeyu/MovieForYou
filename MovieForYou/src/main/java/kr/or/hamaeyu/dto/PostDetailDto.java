package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor @Builder @Getter
@ToString @EqualsAndHashCode
public class PostDetailDto {
	private Long id;                      // 게시글 ID
    private String postTitle;                 // 게시글 제목
    private String postContent;               // 게시글 내용
    private String nickname;              // 작성자 닉네임
    private LocalDateTime createdAt;      // 작성일
    private LocalDateTime updatedAt;      // 수정일

	//가공 데이터
	private String createdAtStr;
	private String updatedAtStr;
}
