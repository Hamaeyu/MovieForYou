package kr.or.hamaeyu.action;

import java.io.IOException;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface Action {
	ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException;
}
