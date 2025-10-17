package kr.or.hamaeyu.ajax;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 댓글 달기(비동기)
 */
@WebServlet("/commentok.free")
public class FreeBoardCommentCreateOkController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    public FreeBoardCommentCreateOkController() {
        super();
    }

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}

}
