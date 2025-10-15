package kr.or.hamaeyu.controller;

import java.io.IOException;
import java.util.Random;

import at.favre.lib.crypto.bcrypt.BCrypt;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.dao.UserDAO;
import kr.or.hamaeyu.service.UserService;
import kr.or.hamaeyu.utils.EmailUtil;

@WebServlet("*.user")
public class FrontUserController extends HttpServlet {
	
	
	
	
    private static final long serialVersionUID = 1L;

    public FrontUserController() { super(); }

    private void doProcess(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        response.setContentType("text/plain;charset=UTF-8");

        String uri = request.getRequestURI();
        String command = uri.substring(uri.lastIndexOf("/"));

        Action action = null;
        ActionForward forward = null;

        // ✅ 공통 DAO / Service 한 번만 선언
        UserDAO dao = new UserDAO();
        UserService service = new UserService();

        // ==============================
        // 1️⃣ 회원가입 (폼 + 등록)
        // ==============================
        if ("/signup.user".equals(command)) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                forward = new ActionForward();
                forward.setPath("/WEB-INF/views/signup.jsp");
                forward.setRedirect(false);
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                action = new UserService();
                forward = action.execute(request, response);
            }
        }

        // ==============================
        // 2️⃣ 이메일 중복 확인
        // ==============================
        else if ("/checkEmail.user".equals(command)) {
            String email = request.getParameter("email");
            boolean exists = dao.checkEmailExists(email);
            response.getWriter().write(exists ? "EXISTS" : "OK");
        }

        // ==============================
        // 3️⃣ 닉네임 중복 확인
        // ==============================
        else if ("/checkNickname.user".equals(command)) {
            String nickname = request.getParameter("nickname");
            boolean exists = dao.checkNicknameExists(nickname);
            response.getWriter().write(exists ? "EXISTS" : "OK");
        }

        // ==============================
        // 4️⃣ 이메일 인증 JSP 열기 (팝업)
        // ==============================
        else if ("/email.user".equals(command)) {
            forward = new ActionForward();
            forward.setPath("/WEB-INF/views/email.jsp");
            forward.setRedirect(false);
        }

        // ==============================
        // 5️⃣ 이메일 인증번호 전송 (5분 유효, Gmail SMTP)
        // ==============================
        else if ("/sendVerifyCode.user".equals(command)) {
            String email = request.getParameter("email");
            try {
                String code = String.valueOf(100000 + new Random().nextInt(900000));
                long expireTime = System.currentTimeMillis() + (5 * 60 * 1000);

                request.getSession().setAttribute("verifyCode", code);
                request.getSession().setAttribute("verifyExpire", expireTime);

                String subject = "[SoundWave] 이메일 인증번호입니다.";
                String body = "안녕하세요.\n\n요청하신 인증번호는 [" + code + "] 입니다.\n\n5분 내에 입력해주세요.";
                EmailUtil.sendText(email, subject, body, false);

                response.getWriter().write("SENT");
            } catch (Exception e) {
                e.printStackTrace();
                response.getWriter().write("FAIL");
            }
        }

        // ==============================
        // 6️⃣ 인증번호 검증
        // ==============================
        else if ("/verifyCode.user".equals(command)) {
            String code = request.getParameter("code");
            String saved = (String) request.getSession().getAttribute("verifyCode");
            Long expireTime = (Long) request.getSession().getAttribute("verifyExpire");

            if (saved == null || expireTime == null) {
                response.getWriter().write("EXPIRED");
            } else if (System.currentTimeMillis() > expireTime) {
                response.getWriter().write("EXPIRED");
                request.getSession().removeAttribute("verifyCode");
                request.getSession().removeAttribute("verifyExpire");
            } else if (saved.equals(code)) {
                response.getWriter().write("OK");
                request.getSession().removeAttribute("verifyCode");
                request.getSession().removeAttribute("verifyExpire");
            } else {
                response.getWriter().write("FAIL");
            }
        }

        // ==============================
        // 7️⃣ 비밀번호 찾기 (폼 + 이메일 인증)
        // ==============================
        else if ("/findPw.user".equals(command)) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                forward = new ActionForward();
                forward.setPath("/WEB-INF/views/findPw.jsp");
                forward.setRedirect(false);
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                String email = request.getParameter("email");
                String nickname = request.getParameter("nickname");

                boolean exists = dao.checkUserForPasswordReset(email, nickname);
                response.getWriter().write(exists ? "FOUND" : "NOT_FOUND");
            }
        }

        // ==============================
        // 8️⃣ 비밀번호 재설정 (bcrypt 적용)
        // ==============================
        else if ("/resetPw.user".equals(command)) {
            String email = request.getParameter("email");
            String rawPw = request.getParameter("password");

            String hashedPw = BCrypt.withDefaults().hashToString(12, rawPw.toCharArray());
            boolean result = dao.updatePassword(email, hashedPw);

            if (result) {
                forward = new ActionForward();
                forward.setPath("/WEB-INF/views/resetPwOK.jsp");
                forward.setRedirect(false);
            } else {
                request.setAttribute("errorMsg", "비밀번호 변경에 실패했습니다.");
                forward = new ActionForward();
                forward.setPath("/WEB-INF/views/error.jsp");
                forward.setRedirect(false);
            }
        }

        // ==============================
        // 9️⃣ 결과 페이지 이동 (forward or redirect)
        // ==============================
        if (forward != null) {
            if (forward.isRedirect()) {
                response.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(forward.getPath());
                rd.forward(request, response);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doProcess(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doProcess(request, response);
    }
}
