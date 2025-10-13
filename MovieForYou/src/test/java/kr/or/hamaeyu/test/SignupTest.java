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
public class SignupTest {
    @Test
    void createTestUsers() {
        Connection conn = null;

        try {
            conn = ConnectionPoolHelper.getConnection();
            conn.setAutoCommit(false); // 트랜잭션 시작
            log.info("[INFO] DB 연결 성공, 트랜잭션 시작");

            // --------------------------
            // 1️⃣ ADMIN 계정 생성
            // --------------------------
            insertUser(conn, "admin@example.com", "Admin123!", "관리자", 1);

            // --------------------------
            // 2️⃣ USER 계정 생성
            // --------------------------
            insertUser(conn, "user@example.com", "User12345!", "일반회원", 2);

            // --------------------------
            // 3️⃣ 트랜잭션 커밋
            // --------------------------
            conn.commit();
            log.info("[INFO] 트랜잭션 커밋 완료, 테스트 계정 생성 완료!");

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

    /**
     * 단일 사용자 삽입
     */
    private void insertUser(Connection conn, String email, String rawPassword, String nickname, int roleId) throws SQLException {
        String hashedPassword = BCrypt.withDefaults().hashToString(12, rawPassword.toCharArray());
        log.info("[INFO] 비밀번호 해시 생성: {}", hashedPassword);

        String sql = "INSERT INTO app_user(email, password_hash, nickname, role_id) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ps.setString(2, hashedPassword);
            ps.setString(3, nickname);
            ps.setInt(4, roleId);
            int inserted = ps.executeUpdate();
            log.info("[INFO] app_user insert 수행: {} row, email={}, role_id={}", inserted, email, roleId);
        }
    }
}
