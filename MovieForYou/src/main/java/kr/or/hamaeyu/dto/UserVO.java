package kr.or.hamaeyu.dto;

import java.sql.Timestamp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserVO {
    private Long userIdx;        // ID (PK)
    private String email;        // 로그인용 아이디
    private String password;     // PASSWORD_HASH
    private String nickname;     // 닉네임 UNIQUE
    private String status;       // IS_DELETED (Y/N 그대로)
    private Timestamp regDate;   // CREATED_AT
    private int roleId;          // ROLE_ID

    public UserVO(String email, String password, String nickname) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "UserVO{" +
                "userIdx=" + userIdx +
                ", email='" + email + '\'' +
                ", nickname='" + nickname + '\'' +
                ", regDate=" + regDate +
                ", status='" + status + '\'' +
                '}';
    }
}