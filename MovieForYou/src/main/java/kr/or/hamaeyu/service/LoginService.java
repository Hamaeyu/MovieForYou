package kr.or.hamaeyu.service;

import java.util.Optional;

import at.favre.lib.crypto.bcrypt.BCrypt;
import at.favre.lib.crypto.bcrypt.BCrypt.Result;
import kr.or.hamaeyu.dao.LoginDao;
import kr.or.hamaeyu.dto.LoginRequest;
import kr.or.hamaeyu.dto.LoginResponse;
import kr.or.hamaeyu.exception.AuthenticationException;
import kr.or.hamaeyu.model.Role;
import kr.or.hamaeyu.model.User;

public class LoginService {
	//싱글톤으로 구현 - 요청마다 달라지는 상태(필드)를 저장하지 않기 때문에
	// 굳이 요청마다 객체 생성 할 필요 없다고 판단함
	private static final LoginService instance;
	
	private final LoginDao loginDao;
	
	static {
		instance = new LoginService();
	}
	
	private LoginService() { // 외부 호출 막음
		loginDao = LoginDao.getInstance();
	}
	
	public static LoginService getInstance() {
		return instance;
	}
	
	public LoginResponse login(LoginRequest dto) {
		//로그인 시도한 email로 사용자 조회
		Optional<User> userOpt = loginDao.selectByEmail(dto.getEmail());
		User user = userOpt.orElseThrow(() -> new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다."));
		//orElseThrow : 조회해서 없으면 예외 던지고, 있으면 Optional 껍질 벗겨서 객체만 반환함
		
		if(!checkPassword(dto.getPassword(), user.getPasswordHash())) {
			throw new AuthenticationException("이메일 또는 비밀번호가 올바르지 않습니다.");
		}
		
		//비밀번호 일치 시 실행됨
		return LoginResponse.builder()
				.id(user.getId())
				.nickname(user.getNickname())
				.role(Role.fromId(user.getRoleId()))
				.build();
	}
	
	private boolean checkPassword(String rawPassword, String passwordHash) {
	    //BCrypt.verifyer()를 사용하여 검증 객체 생성
	    Result result = BCrypt.verifyer().verify(rawPassword.toCharArray(), passwordHash.toCharArray());
		 // 문자열을 char[]로 변환하는 이유: 
		 // 메모리에 남는 문자열보다 char[]가 보안상 안전 (GC 시 빠르게 소멸)
	    
	    //결과 객체에서 verified 상태를 확인
	    return result.verified;
	}
}
