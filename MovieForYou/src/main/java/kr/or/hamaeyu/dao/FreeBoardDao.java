package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import kr.or.hamaeyu.dto.PostDetailDto;
import kr.or.hamaeyu.dto.PostListDto;
import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.Post;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FreeBoardDao {
	private static final FreeBoardDao instance;
	static {
		instance = new FreeBoardDao();
	}
	private FreeBoardDao() {}
	
	public static FreeBoardDao getInstance() {
		return instance;
	}
	
	//SQL 상수 선언
	private static final String POST_INSERT_FREE = 
			"insert into post(id, post_title, post_content, user_id, type_id) "
			+ "values(?, ?, ?, ?, ?)";

	// 비동기로 실행되는 쿼리
	private static final String POST_SELECT_SEQ_NEXTVAL = "select post_id_seq.nextval from dual";

	private static final String POST_SELECT_SEQ_CURRVAL = "select post_id_seq.currval from dual";
	
	private static final String POST_IMAGE_SELECT_JOIN = 
			"select p.id, p.post_title, p.created_at, p.updated_at, pi.image_url, pi.is_thumbnail, u.nickname "
			+ "from post p left join post_image pi on p.id = pi.post_id "
			+ "left join app_user u on p.user_id = u.id "
			+ "where p.type_id = 3 "
			+ "and (is_thumbnail is null or is_thumbnail = 'Y')";
	
	private static final String POST_SELECT_BY_ID =
			"select p.id, p.post_title, p.created_at, p.updated_at, p.post_content, u.nickname "
			+ "from post p left join app_user u ON p.user_id = u.id where p.id = ?";
			
	
	private static Long getNextTempImageId(Connection conn) {
		Long newId = 0L;
		try(PreparedStatement pstmt = conn.prepareStatement(POST_SELECT_SEQ_NEXTVAL);
			ResultSet rs = pstmt.executeQuery();){
			if(rs.next()) {
				newId = rs.getLong(1);
			}
		}catch(SQLException e) {
			log.error("[DB 예외] {}", e.getMessage());
			throw new DataAccessException("임시 이미지 테이블의 다음 시퀀스 조회가 실패했습니다.");
		}
		return newId;
	}
	
	//게시글 insert -> id(pk) 반환
	public Long insertFreeBoard(Connection conn, Post post) {
		Long id = getNextTempImageId(conn);
		log.debug("insertFreeBoard(post={}), id={} ", post, id);
		int result = 0;
		//커넥션 객체는 서비스에서 트랜잭션 처리해야되서 여기서 close 안됨
		//Dao는 단순히 PreparedStatement와 ResultSet만 관리
		try(PreparedStatement pstmt = conn.prepareStatement(POST_INSERT_FREE)){
			pstmt.setLong(1, id);
			pstmt.setString(2, post.getPostTitle());
			pstmt.setString(3, post.getPostContent());
			pstmt.setLong(4, post.getUserId());
			pstmt.setInt(5, post.getTypeId());
			
			result = pstmt.executeUpdate(); // 쿼리 실행
			
			if(result > 0) {
				log.debug("[insert] 성공 건 수 : {}", result);
			}
			
		}catch(SQLException e) {
			log.error("[DB 예외] {}", e.getMessage());
			throw new DataAccessException("DB 자유 게시글 insert 실패", e);
		}
		
		return id;
	}
	
	/**
	 * 자유게시판 목록조회
	 * @return 자유 게시판 목록 리스트
	 */
	public List<PostListDto> selectPostList(){
		List<PostListDto> dto = new ArrayList<PostListDto>();
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(POST_IMAGE_SELECT_JOIN);
				ResultSet rs = pstmt.executeQuery();) {
			log.debug("[selectPostList] 쿼리 실행 완료, 결과 처리 시작");
			while(rs.next()) {
				
				Timestamp createdTs = rs.getTimestamp("created_at");
                Timestamp updatedTs = rs.getTimestamp("updated_at");

                LocalDateTime createdAt = (createdTs != null) ? createdTs.toLocalDateTime() : null;
                LocalDateTime updatedAt = (updatedTs != null) ? updatedTs.toLocalDateTime() : null;

                String imageUrl = rs.getString("image_url") != null ? rs.getString("image_url") : "/images/default.jpg";
                char isThumbnail = rs.getString("is_thumbnail") != null ? rs.getString("is_thumbnail").charAt(0) : 'N';
                log.debug("[selectPostList] row -> id: {}, title: {}, nickname: {}, createdAt: {}, updatedAt: {}, imageUrl: {}, isThumbnail: {}",
                		rs.getLong("id"), rs.getString("post_title"), rs.getString("nickname"), createdAt, updatedAt, imageUrl, isThumbnail);
                
				dto.add(PostListDto.builder()
						.id(rs.getLong("id"))
						.postTitle(rs.getString("post_title"))
						.nickname(rs.getString("nickname"))
						.createdAt(createdAt)
						.updatedAt(updatedAt)
						.imageUrl(imageUrl)
						.isThumnail(isThumbnail)
						.build());
			}
	        if(dto.isEmpty()) {
	            log.warn("[자유게시판 목록 조회] 조회된 게시글이 없습니다.");
	        }
		} catch (SQLException e) {
			log.error("[DB 예외] 자유게시판 목록 조회 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("자유게시판 목록 조회 실패", e);
		}
		
		return dto;
	}
	
	/**
	 * 자유 게시판 상세 보기 단건 조회
	 */
	public PostDetailDto getPostDetail(Long postId) {
		log.debug("[DEBUG] getPostDetail 호출, postId = {}", postId);
		PostDetailDto dto = null;
		try(Connection conn = ConnectionPoolHelper.getConnection();
				PreparedStatement pstmt = conn.prepareStatement(POST_SELECT_BY_ID);) {
			pstmt.setLong(1, postId);
			try(ResultSet rs= pstmt.executeQuery();) {
				if(rs.next()) {
					Timestamp updatedTs = rs.getTimestamp("updated_at");
	                LocalDateTime updatedAt = (updatedTs != null) ? updatedTs.toLocalDateTime() : null;

	                dto= PostDetailDto.builder()
					.id(rs.getLong("id"))
					.postTitle(rs.getString("post_title"))
					.postContent(rs.getString("post_content"))
					.nickname(rs.getString("nickname"))
					.createdAt(rs.getTimestamp("created_at").toLocalDateTime())
					.updatedAt(updatedAt)
					.build();
	                log.debug("[DB] 상세보기 조회 완료: {}", dto);
				}else {
					log.warn("[DB] 자유게시판 상세 보기 - 조회된 행이 없습니다. post.id = {}", postId);
				}
			} catch (SQLException e) {
				log.error("[DB 예외] 자유게시판 상세 보기 select 실행 실패 : {}", e.getMessage(), e);
				throw new DataAccessException("자유게시판 상세보기 조회 실패", e);
			}
		} catch (SQLException e) {
			log.error("[DB 예외] 자유게시판 상세 보기 조회 실패 : {}", e.getMessage(), e);
			throw new DataAccessException("자유게시판 상세보기 조회 실패", e);
		}
		
		return dto;
	}
}
