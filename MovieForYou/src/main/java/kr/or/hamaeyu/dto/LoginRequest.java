package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import kr.or.hamaeyu.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor @AllArgsConstructor
@Builder @Getter @ToString @EqualsAndHashCode
public class LoginRequest {
	private String email;
	private String password;

}
