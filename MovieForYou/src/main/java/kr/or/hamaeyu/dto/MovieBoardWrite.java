package kr.or.hamaeyu.dto;

import java.sql.Date;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor 
@AllArgsConstructor
@Builder @Getter @ToString
public class MovieBoardWrite {
	private String reviewName;
	private LocalDateTime watchDate;
	private int rating;
	private String review;
	private String imageFile;
}
