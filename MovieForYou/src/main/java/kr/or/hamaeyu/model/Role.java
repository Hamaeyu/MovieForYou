package kr.or.hamaeyu.model;
/**
 * 사용자 권한
 */

import lombok.AllArgsConstructor;
import lombok.Getter;
@Getter
public enum Role {
	ADMIN(1,"ADMIN"),
	//public static finel ADMIN = new UserRole(1,"ADMIN");처럼 동작
	USER(2, "USER"),
	GUEST(3, "GUEST");
	
	private final int id;
	private final String roleName;
	
	private Role(int id, String roleName) {
		this.id = id;
		this.roleName = roleName;
	}
	
    // id로 enum 가져오기
    public static Role fromId(int id) {
        for(Role role : Role.values()) {
            if(role.id == id) return role;
        }
        throw new IllegalArgumentException("존재하지 않는 role_id: " + id);
    }
}
