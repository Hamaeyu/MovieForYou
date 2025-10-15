package kr.or.hamaeyu.exception;

/**
 * 커스텀 예외 Object Storage 업로드 실패용 예외
 */
public class ObjectStorageException extends RuntimeException {

	public ObjectStorageException(String message) {
		super(message);
	}

	public ObjectStorageException(String message, Throwable cause) {
		super(message, cause);
	}
}
