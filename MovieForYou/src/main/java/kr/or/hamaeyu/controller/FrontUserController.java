package kr.or.hamaeyu.controller;
import java.io.IOException;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.service.UserService;

@WebServlet("*.user")
public class FrontUserController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public FrontUserController() {
        super();
    }

    private void doProcess(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String command = uri.substring(uri.lastIndexOf("/"));

        Action action = null;
        ActionForward forward = null;

        if ("/signup.user".equals(command)) {
            if ("GET".equalsIgnoreCase(request.getMethod())) {
                // 회원가입 폼 보여주기
                forward = new ActionForward();
                forward.setPath("/WEB-INF/views/signup.jsp");
                forward.setRedirect(false);
            } else if ("POST".equalsIgnoreCase(request.getMethod())) {
                // 회원가입 처리
                action = new UserService();
                forward = action.execute(request, response);
            }
        }

        if (forward != null) {
            if (forward.isRedirect()) {
                response.sendRedirect(forward.getPath());
            } else {
                RequestDispatcher rd = request.getRequestDispatcher(forward.getPath());
                rd.forward(request, response);
            }
        } else {
            // 알 수 없는 요청일 경우 404 또는 에러 처리
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        }
    }


    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doProcess(request, response);
    }
}
