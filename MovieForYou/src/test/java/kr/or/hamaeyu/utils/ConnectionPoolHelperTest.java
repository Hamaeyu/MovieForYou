package kr.or.hamaeyu.utils;

import java.sql.Connection;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ConnectionPoolHelperTest {
	@Test
	public void testConnection() {
		try(Connection conn = ConnectionPoolHelper.getConnection()){
			assertNotNull(conn);// Connection 객체가 null이 아니어야 테스트 통과
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
}
