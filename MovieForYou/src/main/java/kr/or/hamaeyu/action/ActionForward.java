package kr.or.hamaeyu.action;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ActionForward {
	private boolean isRedirect = false; // 뷰의 전환 여부를 결정
	private String path = null;//이동 경로의 주소 관리(forward시킬 경로)
	
}
