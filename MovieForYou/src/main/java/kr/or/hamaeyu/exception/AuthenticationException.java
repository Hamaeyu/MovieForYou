package kr.or.hamaeyu.exception;
/**
 * 커스텀 예외 클래스 
 * 인증 관련 예외로 사용
 */
public class AuthenticationException extends RuntimeException {
	public AuthenticationException(String message) {
        super(message);
    }
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
