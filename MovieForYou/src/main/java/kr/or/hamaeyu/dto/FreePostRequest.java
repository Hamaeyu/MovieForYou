package kr.or.hamaeyu.dto;

import java.util.List;

import jakarta.servlet.http.Part;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
@Getter @NoArgsConstructor @AllArgsConstructor @Builder 
@EqualsAndHashCode @ToString
public class FreePostRequest {
	 private String title;                 // 글 제목
	 private String content;               // 글 내용 (CKEditor HTML 포함)
	 private List<TempImageDto> tempImages;      // 임시 이미지 ID 목록
}
