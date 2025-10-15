package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import kr.or.hamaeyu.dto.UserVO;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class UserDAO {

    public boolean checkEmailExists(String email) {
        String sql = "SELECT COUNT(*) FROM APP_USER WHERE email = ?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, email);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

    public boolean checkNicknameExists(String nickname) {
        String sql = "SELECT COUNT(*) FROM APP_USER WHERE nickname = ?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, nickname);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt(1) > 0;
        } catch (Exception e) { e.printStackTrace(); }
        return false;
    }

  
    
    public boolean insertUser(UserVO user) {
        String sql = "INSERT INTO APP_USER (email, password_hash, nickname, is_deleted, created_at, role_id) VALUES (?, ?, ?, 'N', SYSDATE, 2)";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getPassword());
            pstmt.setString(3, user.getNickname());
            return pstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    
    public boolean updatePassword(String email, String passwordHash) {
        String sql = "UPDATE APP_USER SET PASSWORD_HASH = ? WHERE EMAIL = ?";
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, passwordHash);
            ps.setString(2, email);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // 회원정보 수정 (닉네임 / 비밀번호)
    public int updateUser(UserVO user) {
        String sql = "UPDATE APP_USER SET PASSWORD_HASH = ?, NICKNAME = ? "
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
	        String sql = "SELECT COUNT(*) FROM APP_USER WHERE EMAIL = ? AND NICKNAME = ? AND IS_DELETED = 'N'";
	
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
    
		//  회원 탈퇴 (논리 삭제)
		    public int deleteUser(Long userIdx) {
		        String sql = "UPDATE APP_USER SET IS_DELETED = 'Y' WHERE ID = ?";
		
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
