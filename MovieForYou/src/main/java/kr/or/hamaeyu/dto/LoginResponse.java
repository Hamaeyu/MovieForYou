package kr.or.hamaeyu.dto;

import kr.or.hamaeyu.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @ToString @EqualsAndHashCode
public class LoginResponse {
	private Long id;
	private String nickname;
	private Role role;
	
}
