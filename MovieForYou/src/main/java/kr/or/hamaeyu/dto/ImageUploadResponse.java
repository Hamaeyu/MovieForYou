package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class ImageUploadResponse {
	private boolean success; //업로드 성공 여부
	private Long id; //DB 임시 이미지 PK
	private String imageUrl; //Object Storage URL
	private String message; // 실패 시 응답으로 보낼 메시지
	
}
