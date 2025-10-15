package kr.or.hamaeyu.utils;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.annotation.WebListener;
import jakarta.servlet.ServletContextListener;



/**
 * AppContextListener
 * ------------------
 * 웹 애플리케이션 시작/종료 시 실행되는 리스너
 * HikariCP DataSource를 안전하게 초기화하고 종료하도록 처리
 * 
 * 사용 이유:
 * 웹앱 종료 시 DataSource.close() 호출 → HikariCP 백그라운드 스레드 안전 종료
 *    -> 발생한 IllegalStateException 경고 방지
 */
@WebListener // 톰캣이 자동으로 감지하도록 WebListener 등록
public class AppContextListener implements ServletContextListener{
	/**
     * 웹 애플리케이션 시작 시 호출되는 메서드
     * 톰캣이 contextInitialized 시점에 호출함
     * 
     * @param sce 서블릿 컨텍스트 이벤트 객체
     */
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // DataSource 싱글턴 인스턴스 초기화
        // -> HikariCP 커넥션 풀 생성
        DataSourceUtil.getInstance();

        // 확인용 로그
        System.out.println("DataSource 초기화 완료");
    }

    /**
     * 웹 애플리케이션 종료 시 호출되는 메서드
     * 톰캣이 contextDestroyed 시점에 호출함
     * 
     * @param sce 서블릿 컨텍스트 이벤트 객체
     */
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // DataSource 종료
        // -> HikariCP 백그라운드 커넥션 스레드 안전하게 종료
        // -> IllegalStateException 경고 방지
        DataSourceUtil.getInstance().closeDataSource();
    }

}
