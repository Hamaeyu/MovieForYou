package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

import kr.or.hamaeyu.exception.DataAccessException;
import kr.or.hamaeyu.model.User;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LoginDao {
	//싱글톤으로 구현 - 요청마다 다른 상태(필드)를 저장하지 않기 때문에
	// 굳이 요청마다 객체 생성 할 필요 없다고 판단함
	private static final LoginDao instance;
	
	static {
		instance = new LoginDao();
	}
	
	private LoginDao() {} 

	public static LoginDao getInstance() {
		return instance;
	}
	
	//SQL 상수 선언(재사용)
	private static final String SQL_SELECT_USER = 
			"select id, email, password_hash, nickname, role_id "
			+ "from app_user where email = ? and is_deleted = 'N'";
	
	public Optional<User> selectByEmail(String email){
		log.debug("selectByEmail(email={})", email);
		User user = null;
		//try-with-resource문법 사용 : 자동으로 리소스 반환해줌(명시적 close()필요없음 )
		try(Connection conn = ConnectionPoolHelper.getConnection();
				 PreparedStatement pstmt = conn.prepareStatement(SQL_SELECT_USER);){
			//PreparedStatement : sql문을 DB에 전달해서 실행계획 만듬(재사용)
			pstmt.setString(1, email); //바인드 변수 적용
			
			try(ResultSet rs = pstmt.executeQuery()){
				if(rs.next()) {
					//결과가 있으면 실행
					user = User.builder()
							.id(rs.getLong("id"))
							.email(rs.getString("email"))
							.passwordHash(rs.getString("password_hash"))
							.nickname(rs.getString("nickname"))
							.roleId(rs.getInt("role_id"))
							.build();
				}else {
					log.debug("selectByEmail(email={}) : 조회된 결과가 없습니다.", email);		
				}
			}
		} catch(SQLException e) {
			log.error("[DB 예외] selectByEmail : {}", e.getMessage());
			throw new DataAccessException("DB 조회 실패", e);
		}
		
		return Optional.ofNullable(user); // null이어도 안전하게 처리
		//null이든 아니든 Optional 객체를 생성함
		//null이면 Optional.empty()반환
		//null아니면 Optional.of(user)반환
		//NullPointerException 안 터지게 하려고 사용
	}
	
}
