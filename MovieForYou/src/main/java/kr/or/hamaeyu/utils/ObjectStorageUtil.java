package kr.or.hamaeyu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.UUID;

import com.oracle.bmc.model.BmcException;
import com.oracle.bmc.objectstorage.ObjectStorageClient;
import com.oracle.bmc.objectstorage.requests.PutObjectRequest;
import com.oracle.bmc.objectstorage.requests.GetNamespaceRequest;
import com.oracle.bmc.objectstorage.requests.CreatePreauthenticatedRequestRequest;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails;
import com.oracle.bmc.objectstorage.model.CreatePreauthenticatedRequestDetails.AccessType;
import com.oracle.bmc.objectstorage.responses.PutObjectResponse;
import com.oracle.bmc.objectstorage.responses.GetNamespaceResponse;
import com.oracle.bmc.objectstorage.responses.CreatePreauthenticatedRequestResponse;

import lombok.extern.slf4j.Slf4j;

/**
 * OCI Object Storage 유틸리티
 * - 파일 업로드 + PAR 생성
 * - Private 버킷 + Pre-Authenticated Request(PAR) 사용
 * - 업로드 시마다 PAR URL 반환 (에디터 src용)
 */
@Slf4j
public class ObjectStorageUtil {

    private static final String BUCKET_NAME = "freeboard-images";
    private static final String REGION = "ap-osaka-1";

    /**
     * 파일 업로드 후 PAR URL 생성
     *
     * @param file        업로드할 파일
     * @param objectName  Object Storage에 저장될 객체 이름 (확장자 포함)
     * @param expireDays  PAR 유효기간 (일 단위)
     * @return PAR URL
     */
    public static String uploadFileAndGetParUrl(File file, String objectName, int expireDays) {
        ObjectStorageClient client = null;
        FileInputStream fis = null;

        try {
            // 1. 클라이언트 생성
            client = OCIClientHelper.getClient();

            // 2. 네임스페이스 조회
            GetNamespaceResponse nsResp = client.getNamespace(GetNamespaceRequest.builder().build());
            String namespace = nsResp.getValue();

            // 3. UUID 추가한 고유 파일명 생성
            String uniqueName = UUID.randomUUID() + "_" + objectName;

            // 4. MIME 타입 감지 (fallback 포함)
            String contentType = Files.probeContentType(file.toPath());
            if (contentType == null) {
                String ext = objectName.substring(objectName.lastIndexOf(".") + 1).toLowerCase();
                switch (ext) {
                    case "jpg":
                    case "jpeg": contentType = "image/jpeg"; break;
                    case "png": contentType = "image/png"; break;
                    case "gif": contentType = "image/gif"; break;
                    default: contentType = "application/octet-stream";
                }
            }

            // 5. 파일 업로드
            fis = new FileInputStream(file);
            PutObjectRequest putReq = PutObjectRequest.builder()
                    .namespaceName(namespace)
                    .bucketName(BUCKET_NAME)
                    .objectName(uniqueName)
                    .contentType(contentType)
                    .putObjectBody(fis)
                    .build();
            PutObjectResponse putResp = client.putObject(putReq);
            log.info("[OCI 업로드 성공] 파일명: {} / ETag: {}", file.getName(), putResp.getETag());

            // 6. PAR 생성
            LocalDateTime expireAt = LocalDateTime.now().plusDays(expireDays);
            Timestamp expireTimestamp = Timestamp.valueOf(expireAt);

            CreatePreauthenticatedRequestDetails parDetails = CreatePreauthenticatedRequestDetails.builder()
                    .name("ck-upload-" + uniqueName)
                    .objectName(uniqueName)
                    .accessType(AccessType.ObjectRead)
                    .timeExpires(new java.util.Date(expireTimestamp.getTime()))
                    .build();

            CreatePreauthenticatedRequestRequest parReq = CreatePreauthenticatedRequestRequest.builder()
                    .bucketName(BUCKET_NAME)
                    .namespaceName(namespace)
                    .createPreauthenticatedRequestDetails(parDetails)
                    .build();

            CreatePreauthenticatedRequestResponse parResp = client.createPreauthenticatedRequest(parReq);
            String parUrl = "https://objectstorage." + REGION + ".oraclecloud.com"
                    + parResp.getPreauthenticatedRequest().getAccessUri();

            log.info("[PAR 생성 완료] {}", parUrl);
            return parUrl;

        } catch (BmcException e) {
            log.error("[OCI 업로드/Par 실패 - BMC] 상태코드: {}, 메시지: {}", e.getStatusCode(), e.getMessage(), e);
            return null;
        } catch (IOException e) {
            log.error("[OCI 업로드 실패 - IO] 파일: {} / 오류: {}", file.getName(), e.getMessage(), e);
            return null;
        } finally {
            if (client != null) client.close();
            try { if (fis != null) fis.close(); } catch (IOException ignore) {}
        }
    }
}
