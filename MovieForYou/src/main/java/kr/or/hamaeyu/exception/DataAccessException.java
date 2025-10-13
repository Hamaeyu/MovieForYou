package kr.or.hamaeyu.exception;
/**
 * 커스텀 예외 
 * Dao에서 SQLException발생 시 서비스로 던짐 처리 함
 */
public class DataAccessException extends RuntimeException {
	public DataAccessException(String message) {
        super(message);
    }
    public DataAccessException(String message, Throwable cause) {
        super(message, cause);
    }
}
