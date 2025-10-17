package kr.or.hamaeyu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@NoArgsConstructor @AllArgsConstructor
@Builder @Data @EqualsAndHashCode
public class AdminChart {
	int reviewBoard;
	int freeBoard;
	int cinemaReviewBoard;
}
