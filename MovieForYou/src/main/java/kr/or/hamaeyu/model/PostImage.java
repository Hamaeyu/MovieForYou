package kr.or.hamaeyu.model;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
/**
 * 자유게시판에서 사용
 * 용도 : EKEditor 사용 시 썸네일 이미지 관리용 테이블과 매핑되는 모델 클래스
 */
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode @ToString @Getter
public class PostImage {
	private Long id;
	private Long postId;
	private String imageUrl;
	/**
	 * check 제약 걸어둠('Y'/'N')
	 */
	private char isThumbnail; //썸네일 이미지 여부
	private LocalDateTime uploadedAt;
	
}
