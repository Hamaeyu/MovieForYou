package kr.or.hamaeyu.utils;

import java.io.IOException;

import com.oracle.bmc.Region;
import com.oracle.bmc.auth.ConfigFileAuthenticationDetailsProvider;
import com.oracle.bmc.objectstorage.ObjectStorageClient;

/**
 * OCI ObjectStorageClient 
 * 요청 시마다 클라이언트를 생성해야함
 */
public class OCIClientHelper {

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
    public static ObjectStorageClient createClient() throws IOException {
            // ConfigFileAuthenticationDetailsProvider: OCI SDK 인증용 제공자
            // 첫 번째 파라미터: oci config 파일 경로
            // 두 번째 파라미터: 프로파일 이름 (보통 "DEFAULT")
            ConfigFileAuthenticationDetailsProvider provider =
                    new ConfigFileAuthenticationDetailsProvider("C:\\oci_config", "DEFAULT");

            // ObjectStorageClient 생성 - Builder 패턴 사용
            ObjectStorageClient client = ObjectStorageClient.builder()
                    .build(provider); //생성자 사용 시 경고 발생

            // 사용할 리전 고정
            client.setRegion(Region.AP_OSAKA_1); 
            return client;
        }

}
