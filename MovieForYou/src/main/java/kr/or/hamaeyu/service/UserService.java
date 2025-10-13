package kr.or.hamaeyu.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.dao.UserDAO;
import kr.or.hamaeyu.dto.UserVO;

public class UserService implements Action {

    private final UserDAO userDAO = new UserDAO();

    @Override
    public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
        String email = request.getParameter("email");
        String rawPassword = request.getParameter("password");
        String nickname = request.getParameter("nickname");

        // 비밀번호 암호화 (bcrypt)
        String hashedPassword = BCrypt.withDefaults()
                                      .hashToString(12, rawPassword.toCharArray());

        // 유저 객체 생성 및 권한 설정
        UserVO user = new UserVO(email, hashedPassword, nickname);
        user.setRoleId(2); // 일반 사용자

        boolean result = registerUser(user);

        ActionForward forward = new ActionForward();
        if (result) {
            forward.setPath("/WEB-INF/views/signupOK.jsp");
        } else {
            request.setAttribute("errorMsg", "회원가입에 실패했습니다. 이메일 또는 닉네임이 중복되었을 수 있습니다.");
            forward.setPath("/WEB-INF/views/error.jsp");
        }
        forward.setRedirect(false); // forward 방식 사용
        return forward;
    }

    // 회원가입 DAO 호출
    public boolean registerUser(UserVO user) {
        if (userDAO.checkEmailExists(user.getEmail())) return false;
        if (userDAO.checkNicknameExists(user.getNickname())) return false;

        int result = userDAO.insertUser(user);
        return result > 0;
    }

    // 추가 기능 (선택)
    // public boolean updateUser(UserVO user) { ... }
    // public boolean resetPassword(String email, String nickname, String newPw) { ... }


	 
	 
	 
//
//    //  회원정보 수정
//    public boolean updateUser(UserVO user) {
//        int result = userDAO.updateUser(user);
//        return result > 0;
//    }
//
//   
//
//    //  비밀번호 찾기 및 변경
//    public boolean resetPassword(String email, String nickname, String newPw) {
//        boolean exists = userDAO.checkUserForPasswordReset(email, nickname);
//        if (!exists) return false;
//
//        int result = userDAO.updatePassword(email, newPw);
//        return result > 0;
//    }
//
//    //  회원 정보 조회
//    public Optional<UserVO> getUserById(int userIdx) {
//        return userDAO.getUserById(userIdx);
//    }
//
//    //  회원 탈퇴
//    public boolean deleteUser(Long userIdx) {
//        int result = userDAO.deleteUser(userIdx);
//        return result > 0;
//    }
//
//	@Override
//	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
//		// TODO Auto-generated method stub
//		return null;
//	}
}
