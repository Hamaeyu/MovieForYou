package kr.or.hamaeyu.exception;
/**
 * 커스텀 예외 클래스 
 * 서비스에서 로그인 실패 시 컨트롤러로 던지는데 사용함
 */
public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
