package kr.or.hamaeyu.action.impl;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.hamaeyu.action.Action;
import kr.or.hamaeyu.action.ActionForward;
import kr.or.hamaeyu.dto.PostListDto;
import kr.or.hamaeyu.service.FreeBoardService;

/**
 * 자유게시판 목록 보여주는 용도
 */
public class FreeListAction implements Action{
	private final FreeBoardService freeSvc;
	
	public FreeListAction(){
		freeSvc = FreeBoardService.getInstance();
	}
	
	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response) {
		List<PostListDto> processedList = new ArrayList<>();
		List<PostListDto> list = freeSvc.getFreeBoardList();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		for (PostListDto post : list) {
		    // 닉네임 가공
		    String shortNickname = post.getNickname();
		    String oneNickname = post.getNickname();
		    if (shortNickname != null && shortNickname.length() > 4) {
		        shortNickname = shortNickname.substring(0, 4) + "...";
		    }
		    if (oneNickname != null) {
		        shortNickname = shortNickname.substring(0, 1);
		    }

		    // 날짜 가공
		    String createdAtStr = "";
		    String updatedAtStr = "";
		    if (post.getCreatedAt() != null) {
		        createdAtStr = post.getCreatedAt().format(formatter);
		    }
		    if (post.getUpdatedAt() != null) {
		    	updatedAtStr = post.getUpdatedAt().format(formatter);
		    }

		    // 새 DTO 객체 생성
		    PostListDto dto = PostListDto.builder()
		            .id(post.getId())
		            .postTitle(post.getPostTitle())
		            .shortNickname(shortNickname)
		            .oneNickname(oneNickname)
		            .createdAtStr(createdAtStr) // DTO에 가공용 필드 필요
		            .updatedAtStr(updatedAtStr)
		            .imageUrl(post.getImageUrl())
		            .build();

		    processedList.add(dto);
		}
		
		request.setAttribute("freeList", processedList); //request 속성에 담아서 JSP 전달
		
		return new ActionForward(false,"/WEB-INF/views/board/free/freeList.jsp");
		// 리다이렉트 여부 false, 포워드 시킬 뷰 경로 지정
	}

}
