package kr.or.hamaeyu.model;
/**
 * 데이터베이스의 app_user와 매핑되는 모델 클래스
 */

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @Setter @ToString @EqualsAndHashCode
public class User {
	private Long id;
	private String email;
	private String passwordHash;
	private String nickname;
	private char isDeleted;
	private LocalDateTime createdAt;
	private int roleId;
}
