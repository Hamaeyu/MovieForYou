package kr.or.hamaeyu.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

/**
 * HikariCP DataSource 싱글턴 유틸 클래스
 * 
 * db.properties 파일에서 설정 읽음
 * DataSource 풀 생성 후, DAO 등에서 getConnection()으로 커넥션 사용 가능
 * -> ConnectionPoolHelper를 통해 커넥션 사용
 * JDBC 연결 정보 변경 시 db.properties만 수정하면 됨
 * 
 */
public class DataSourceUtil {
	private static DataSourceUtil instance = null;
	private HikariDataSource ds;
	//히카리cp가 관리하는 커넥션 풀 객체를 담는 변수
	
	private DataSourceUtil() {//외부에서 new못함(private)
		Properties props = new Properties();
		//db.properies읽어오기 위해서 Properties 객체 생성함
		
		//클래스 패스에서 db.properties 파일 읽기
		InputStream is = getClass().getClassLoader().getResourceAsStream("db.properties");
		if(is != null) { //db.properties파일이 존재하면
			try {
				props.load(is);//InputStream에서 바이트를 읽음
				// 줄바꿈 기준으로 1줄씩 key=value로 파싱
				//Properties 객체의 Map에 저장
				
				HikariConfig config = new HikariConfig(props);
				//히카리 설정 객체 생성
				ds = new HikariDataSource(config);// 커넥션 풀 생성
			} catch (IOException e) {
				 e.printStackTrace();
				 throw new RuntimeException("db.properties 파일을 찾을 수 없습니다.");
			}finally {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	//멀티스레드 안전하게 싱글톤 생성
	public static DataSourceUtil getInstance() {
		 if (instance == null) { // 1차 체크 (성능 향상)
		        synchronized (DataSourceUtil.class) {//한 번에 하나의 스레드만 블록 안 코드 실행 가능
		        	//동시에 여러 스레드가 접근하면 나머지는 블록이 풀릴 때까지 대기
		        	//synchronized : 여러 스레드가 동시에 getInstance() 호출해도 
		        	//인스턴스가 2개 이상 생기지 않게 막음.
		            if (instance == null) { // 2차 체크 (Thread Safe)
		                instance = new DataSourceUtil();
		            }
		        }
		    }
		return instance;
	}
	
	 // HikariCP DataSource 반환
	public HikariDataSource getDataSource() {
		return ds;
	}
	
	/**
     * HikariCP DataSource 안전 종료
     * 웹 애플리케이션 종료 시 반드시 호출해야 함
     *
     * - 백그라운드 커넥션 스레드 종료
     * - IllegalStateException 경고 방지
     */
    public void closeDataSource() {
        if (ds != null && !ds.isClosed()) {
            ds.close();
            System.out.println("HikariCP DataSource 종료 완료");
        }
    }
}
