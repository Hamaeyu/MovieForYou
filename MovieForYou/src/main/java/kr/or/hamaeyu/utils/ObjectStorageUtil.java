package kr.or.hamaeyu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.requests.GetObjectRequest;
import com.oracle.bmc.objectstorage.requests.DeleteObjectRequest;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.oracle.bmc.objectstorage.responses.GetObjectResponse;
import com.oracle.bmc.objectstorage.responses.DeleteObjectResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * OCI Object Storage 유틸리티 - 파일 업로드 - 버킷 생성 - 네임스페이스 조회 - 삭제, URL 반환 등
 * Visibility: Private으로 해놨음
 * CKEditor 업로드된 이미지의 URL을 <img> 태그의 src로 삽입
 * 랜더링 하려면 Private은 불가함
 * Private 버킷 + Pre-Authenticated Request(PAR) 사용
 */
@Slf4j
public class ObjectStorageUtil {

	/**
	 * OCI Object Storage 버킷 이름 oci_config에서 읽어올 수도 있음
	 */
	private static final String BUCKET_NAME = "freeboard-images";

	// region
	private static final String REGION = "ap-osaka-1";

	/**
	 * 파일을 OCI Object Storage에 업로드
	 * 
	 * @param file       업로드할 파일 객체
	 * @param objectName Object Storage에 저장될 객체 이름
	 * @return 업로드된 Object URL (성공 시), 실패 시 null
	 */
	public static String uploadFile(File file, String objectName) {
		ObjectStorageClient client = null;
		FileInputStream fis = null;
		try {
			// 1️⃣ 클라이언트 생성
			client = OCIClientHelper.getClient();

			// 2️⃣ 네임스페이스 조회 (모든 Object Storage 작업에 필요)
			GetNamespaceResponse namespaceResp = client.getNamespace(GetNamespaceRequest.builder().build());
			String namespace = namespaceResp.getValue();

			// UUID 추가한 고유 파일명 생성 - 중복 방지용
			String uniqueName = UUID.randomUUID() + "_" + objectName;

			// MIME 타입 자동 감지 (jpg, png 등)
			String contentType = Files.probeContentType(file.toPath());
			if (contentType == null) {
				contentType = "application/octet-stream"; // 감지 실패 시 기본값
			}

			// 파일 스트림 준비
			fis = new FileInputStream(file);

			// 3 업로드 요청 생성
			PutObjectRequest request = PutObjectRequest.builder()
                    .namespaceName(namespace)
                    .bucketName(BUCKET_NAME)
                    .objectName(uniqueName)
                    .contentType(contentType)
                    .putObjectBody(fis)
                    .build();

			// 4 업로드 실행
			 PutObjectResponse response = client.putObject(request);
	         log.info("[OCI 업로드 성공] 파일명: {} / ETag: {}", file.getName(), response.getETag());

			// 5 업로드된 파일의 URL 생성
	            String url = String.format(
	                    "https://objectstorage.%s.oraclecloud.com/n/%s/b/%s/o/%s",
	                    REGION, namespace, BUCKET_NAME, uniqueName
	            );

			return url;

		} catch (BmcException e) {
		    log.error("[OCI 업로드 실패 - BMC 예외] 상태코드: {}, 메시지: {}", e.getStatusCode(), e.getMessage(), e);
		    return null;
		} catch (IOException e) {
		    log.error("[OCI 업로드 실패 - IO 예외] 파일: {} / 오류: {}", file.getName(), e.getMessage(), e);
		    return null;
		} finally {
            // 리소스 정리
            if (client != null) client.close();
            try {
                if (fis != null) fis.close();
            } catch (IOException ignore) {}
		}
	}

}
