package kr.or.hamaeyu.utils;

import java.io.IOException;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

/**
 * OCI ObjectStorageClient 싱글톤 관리 클래스
 * 
 * 목적:
 * - SDK 클라이언트를 매번 새로 생성하지 않고 공유
 * - 리소스 효율성 확보
 * - 모든 서비스/DAO에서 동일 클라이언트 사용 가능
 * 
 * 사용 시 주의:
 * - 멀티스레드 환경에서 안전함 (SDK 자체가 thread-safe)
 * - region, credentials 설정은 최초 1회만 수행됨
 */
public class OCIClientHelper {
	// 싱글톤 클라이언트 객체
    private static ObjectStorageClient client;

    // 외부에서 인스턴스 생성을 막음
    private OCIClientHelper() {}
    
    /**
     * OCI ObjectStorageClient 반환
     *
     * 첫 호출 시:
     * - config 파일 읽음 (OCI 계정 정보, API Key)
     * - ObjectStorageClient 생성
     * - Region 설정
     *
     * 이후 호출 시:
     * - 이미 생성된 클라이언트 반환 (싱글톤)
     *
     * @return ObjectStorageClient 싱글톤 인스턴스
     * @throws IOException config 파일 읽기 실패 시
     */
    public static ObjectStorageClient getClient() throws IOException {
        if (client == null) {
            // ConfigFileAuthenticationDetailsProvider: OCI SDK 인증용 제공자
            // 첫 번째 파라미터: oci config 파일 경로
            // 두 번째 파라미터: 프로파일 이름 (보통 "DEFAULT")
            ConfigFileAuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider("C:\\oci_config", "DEFAULT");

            // ObjectStorageClient 생성 - Builder 패턴 사용
            client = ObjectStorageClient.builder()
                    .build(provider); //생성자 사용 시 경고 발생

            // 사용할 리전 고정
            client.setRegion(Region.AP_OSAKA_1); 
        }

        return client;
    }
    
    /**
     * 싱글톤 클라이언트 종료 시 호출
     * - 애플리케이션 종료 시 리소스 해제 필요
     */
    public static void closeClient() {
        if (client != null) {
            client.close(); // HTTP 연결 및 리소스 해제
            client = null;
        }
    }


}
