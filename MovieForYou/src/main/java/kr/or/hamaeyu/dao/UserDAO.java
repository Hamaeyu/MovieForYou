package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import kr.or.hamaeyu.dto.UserVO;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class UserDAO {

    // 이메일 중복 확인
    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 닉네임 중복 확인
    public boolean checkNicknameExists(String nickname) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE NICKNAME = ?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nickname);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    //  회원가입
    public int insertUser(UserVO user) {
        String sql = "INSERT INTO USERS (EMAIL, PASSWORD_HASH, NICKNAME, IS_DELETED, CREATED_AT, ROLE_ID) "
                   + "VALUES (?, ?, ?, 'N', NOW(), ?)";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            pstmt.setInt(4, user.getRoleId());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    // 회원정보 수정 (닉네임 / 비밀번호)
    public int updateUser(UserVO user) {
        String sql = "UPDATE USERS SET PASSWORD_HASH = ?, NICKNAME = ? "
                   + "WHERE ID = ? AND IS_DELETED = 'N'";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, user.getPassword());
            pstmt.setString(2, user.getNickname());
            pstmt.setLong(3, user.getUserIdx());

            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    

    // 비밀번호 변경용 사용자 확인 (닉네임 + 이메일)
    public boolean checkUserForPasswordReset(String email, String nickname) {
        String sql = "SELECT COUNT(*) FROM USERS WHERE EMAIL = ? AND NICKNAME = ? AND IS_DELETED = 'N'";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, email);
            pstmt.setString(2, nickname);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) return rs.getInt(1) > 0;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    // 비밀번호 재설정
    public int updatePassword(String email, String newPwHash) {
        String sql = "UPDATE USERS SET PASSWORD_HASH = ? WHERE EMAIL = ? AND IS_DELETED = 'N'";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPwHash);
            pstmt.setString(2, email);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //  회원 상세 조회 (회원번호로)
    public Optional<UserVO> getUserById(int userIdx) {
        String sql = "SELECT * FROM USERS WHERE ID = ? AND IS_DELETED = 'N'";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, userIdx);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    UserVO user = new UserVO();
                    user.setUserIdx(rs.getLong("ID"));
                    user.setEmail(rs.getString("EMAIL"));
                    user.setPassword(rs.getString("PASSWORD_HASH"));
                    user.setNickname(rs.getString("NICKNAME"));
                    user.setStatus(rs.getString("IS_DELETED"));
                    user.setRegDate(rs.getTimestamp("CREATED_AT"));
                    user.setRoleId(rs.getInt("ROLE_ID"));
                    return Optional.of(user);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    //  회원 탈퇴 (논리 삭제)
    public int deleteUser(Long userIdx) {
        String sql = "UPDATE USERS SET IS_DELETED = 'Y' WHERE ID = ?";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setLong(1, userIdx);
            return pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
}
