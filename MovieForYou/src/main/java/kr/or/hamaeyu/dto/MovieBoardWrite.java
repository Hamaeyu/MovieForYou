package kr.or.hamaeyu.dto;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor 
@AllArgsConstructor
@Builder @Getter @Setter @ToString
public class MovieBoardWrite {
	private int id;
	private String title;
	private String review;
	private String shortReview;
	private int starRating;
	private long userId;
	private int typeId;
	private LocalDateTime createdAt;
}
