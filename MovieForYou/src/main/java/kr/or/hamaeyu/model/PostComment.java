package kr.or.hamaeyu.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @ToString @EqualsAndHashCode
public class PostComment {
	private Long id;
	private String commentContent;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long postId;
	private Long userId;
}
