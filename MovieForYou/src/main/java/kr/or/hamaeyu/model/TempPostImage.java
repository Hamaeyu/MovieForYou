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
 * 에디터 이미지 비동기에 이용되는 임시 테이블
 * 사용자가 작성중 이미지 업로드를 취소할 수 있어서 사용함
 * 작성 완료 버튼 시 -> post_image테이블로 마이그레이션 함
 * 
 * -- 1행당 1이미지
 *    썸네일 이미지에만 is_thumbnail -> 'Y'들어감
   -- temp_uuid 가 “같은 업로드 세션”을 묶는 역할
   -- UUID 값으로 묶어서 같은 레코드가 하나의 게시글에 속하게 되는 구조
 */
@NoArgsConstructor @AllArgsConstructor @Builder
@EqualsAndHashCode @ToString @Getter
public class TempPostImage {
	private Long id;
	private String tempUuid;
	private String imageUrl;
	/**
	 * 오라클에서 char타입 공백 이슈 있음
	 * rs.getString().trim()
	 * 하지만, 
	 * check 제약 걸어둠('Y'/'N')
	 * ->'Y '처럼 공백이 붙은 값은 아예 INSERT 불가
	 */
	private char isThumbnail; //썸네일 이미지 여부
	private LocalDateTime uploadedAt;
	/*
	 -- 사용자가 글 등록 시 post_image에 마이그레이션 post_id컬럼 fk값 연결 필수
	 -- DB와 오브젝트 스토리지 간 트랜잭션 하지 못하므로 무결성에 문제 생기지 않도록 주의해서 작업하기 
	 
	 temp_post_image → post_image로 옮기는 순간은 게시글 등록 트랜잭션 안에서 수행해야 함.
	 */
}
