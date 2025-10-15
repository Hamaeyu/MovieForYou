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
public class Post {
	private Long id;
	private String postTitle;
	private String postContent;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;
	private Long userId;
	private Integer typeId;
}
