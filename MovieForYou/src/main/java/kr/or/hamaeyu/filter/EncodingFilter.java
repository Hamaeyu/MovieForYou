package kr.or.hamaeyu.filter;

import java.io.IOException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * 모든 요청을 대상으로 문자 인코딩(UTF-8) 필터
 */
@Slf4j
public class EncodingFilter extends HttpFilter {
	private static final long serialVersionUID = 1L;
	private String encoding;  // 인코딩 타입 (web.xml <param-value>에서 가져옴)
	
	//WAS가 Filter 객체를 생성한 후, 필터의 초기화 작업을 하기 위해서 호출 하는 메서드
		@Override
		public void init(FilterConfig filterConfig) throws ServletException {
			//web.xml에서 <filter>의 <param-name> 값을 아규먼트로 전달하면,
			//<filter>의 <param-value> 값을 리턴해줌.
			encoding = filterConfig.getInitParameter("encoding"); //web.xml에 있는 <param-name>encoding</param-name> 이름과 같은걸로 아규먼트로 줘야함
			//<param-value>UTF-8</param-value>해놓음
			log.debug("init:encoding={}",encoding);
		}
		
		// 필드 체인(-> 서블릿)으로 진행하기 위해서 WAS가 호출하는 메서드.
		@Override
		public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
				throws IOException, ServletException {
			
			HttpServletRequest req = (HttpServletRequest) request;
			//정적 리소스 제외
	        if (req.getRequestURI().matches(".*(\\.css|\\.js|\\.png|\\.jpg|\\.jpeg|\\.gif|\\.woff|\\.woff2|\\.ttf)$")) {
	        	chain.doFilter(request, response); // 현재 인코딩 필터 건너뛰고 다음 필터/서블릿 실행
	            return;
	        }
			//요청(request) 객체의 문자열 인코딩 타입을(UTF-8)로 설정:
			request.setCharacterEncoding(encoding);
			
			//응답 인코딩 설정
			response.setCharacterEncoding(encoding);
			
			//content-Type은 각 서블릿에서 설정
			
			 log.debug("doFilter: request/response encoding set to {}", encoding);
			
			//다음 필터 체인을 진행하거나 서블릿 메서드 호출
			chain.doFilter(request, response);
		}
}
