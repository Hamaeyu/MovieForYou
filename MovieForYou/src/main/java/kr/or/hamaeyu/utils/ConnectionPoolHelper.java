package kr.or.hamaeyu.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DataSourceUtil에서 생성한 HikariCP 풀에서 Connection 빌려줌
 */
public class ConnectionPoolHelper {
	
	//커넥션 빌려오기
	public static Connection getConnection() throws SQLException {
		//히카리 풀에서 커넥션 1개 빌려옴
		return DataSourceUtil.getInstance().getDataSource().getConnection();
	}
	
	//커넥션 반환
	public static void close(Connection conn) {
		if(conn != null) {
			try {
				conn.close(); // 반환함
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//ResultSet 반환 오버로딩
	public static void close(ResultSet rs) {
		if(rs != null) {
			try {
				rs.close(); // 반환함
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
	//PreparedStatement 반환 오버로딩
	public static void close(PreparedStatement pstmt) {
		if(pstmt != null) {
			try {
				pstmt.close(); // 반환함
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
	
    // Statement 반환
    public static void close(Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
		
}
