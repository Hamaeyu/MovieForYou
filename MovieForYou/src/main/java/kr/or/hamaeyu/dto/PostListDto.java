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
public class PostListDto {
	private Long id; // post.id
	private String postTitle; // post.post_title
	private String nickname; // 사용자 닉네임
	private LocalDateTime createdAt; // post.created_at
	private LocalDateTime updatedAt; // post.updated_at
	private String imageUrl; // 썸네일 이미지 URL 또는 기본 이미지
	private char isThumnail; //썸네일 여부
	
	//가공 데이터
	private String createdAtStr;
	private String updatedAtStr;
	private String oneNickname;
	private String shortNickname;
}
