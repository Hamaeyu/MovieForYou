package kr.or.hamaeyu.test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;

import at.favre.lib.crypto.bcrypt.BCrypt;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

/**
 * test용 계정 생성 + BCrypt의존성 확인용 test
 */
@Slf4j
public class SignupTest3 {
	 @Test
	    void signupWithRolesTest() {
	        Connection conn = null;

	        try {
	            conn = ConnectionPoolHelper.getConnection();
	            conn.setAutoCommit(false); // 트랜잭션 시작
	            log.info("[INFO] DB 연결 성공, 트랜잭션 시작");

	            // --------------------------
	            // 1️⃣ 회원가입
	            // --------------------------
	            String email = "test3@example.com";
	            String rawPassword = "test12345@";
	            String nickname = "일반회원";

	            // 비밀번호 해싱
	            String hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
	            log.info("[INFO] 비밀번호 해시 생성: {}", hashedPassword);

	            String insertUserSQL = "INSERT INTO app_user(email, password_hash, nickname) VALUES (?, ?, ?)";
	            PreparedStatement psUser = conn.prepareStatement(insertUserSQL, new String[]{"id"});
	            psUser.setString(1, email);
	            psUser.setString(2, hashedPassword);
	            psUser.setString(3, nickname);
	            int userInsertCount = psUser.executeUpdate();
	            log.info("[INFO] app_user insert 수행: {} row", userInsertCount);

	            // 자동 생성된 ID 가져오기
	            ResultSet rs = psUser.getGeneratedKeys();
	            int userId = -1;
	            if(rs.next()) {
	                userId = rs.getInt(1);
	                log.info("[INFO] 생성된 사용자 ID: {}", userId);
	            }

	            // --------------------------
	            // 2️⃣ 회원 권한 부여 (USER)
	            // --------------------------
	            //int adminRoleId = getRoleId(conn, "ADMIN");
	            int userRoleId = getRoleId(conn, "USER");

	            String insertUserRoleSQL = "INSERT INTO user_role(user_id, role_id) VALUES (?, ?)";
	            PreparedStatement psRole = conn.prepareStatement(insertUserRoleSQL);

//	            // ADMIN 권한
//	            psRole.setInt(1, userId);
//	            psRole.setInt(2, adminRoleId);
//	            int adminInsertCount = psRole.executeUpdate();
//	            log.info("[INFO] user_role ADMIN insert: {} row", adminInsertCount);

	            // USER 권한
	            psRole.setInt(1, userId);
	            psRole.setInt(2, userRoleId);
	            int userInsertCountRole = psRole.executeUpdate();
	            log.info("[INFO] user_role USER insert: {} row", userInsertCountRole);

	            // --------------------------
	            // 3️⃣ 트랜잭션 커밋
	            // --------------------------
	            conn.commit();
	            log.info("[INFO] 트랜잭션 커밋 완료, 회원가입 + 권한 부여 완료!");

	        } catch (Exception e) {
	            try {
	                if (conn != null) {
	                    conn.rollback();
	                    log.warn("[WARN] 트랜잭션 롤백 수행");
	                }
	            } catch (SQLException ex) {
	                log.error("[ERROR] 롤백 실패", ex);
	            }
	            log.error("[ERROR] 테스트 실패", e);
	        } finally {
	            ConnectionPoolHelper.close(conn);
	            log.info("[INFO] DB 연결 종료");
	        }
	    }

	    // role_name으로 role_id 조회
	    private int getRoleId(Connection conn, String roleName) throws SQLException {
	        String sql = "SELECT id FROM role WHERE role_name = ?";
	        try(PreparedStatement ps = conn.prepareStatement(sql)) {
	            ps.setString(1, roleName);
	            ResultSet rs = ps.executeQuery();
	            if(rs.next()) {
	                int roleId = rs.getInt("id");
	                log.info("[INFO] 조회된 role_id ({}): {}", roleName, roleId);
	                return roleId;
	            } else {
	                throw new SQLException(roleName + " 권한이 존재하지 않습니다.");
	            }
	        }
	    }
}
