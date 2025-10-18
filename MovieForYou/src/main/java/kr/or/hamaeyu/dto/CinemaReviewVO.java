package kr.or.hamaeyu.dto;

import lombok.Data;

@Data
public class CinemaReviewVO {
    private long id;                 // POST ID
    private String title;            // 제목
    private String content;          // 서머노트 내용
    private Long userId;             // 작성자 ID
    private String writer;           // 작성자 닉네임
    private String createdAt;        // 작성일
    private String overallReview;    // 총평
    private Integer cinemaRating;    // 상영관 별점
    private Integer seatRating;      // 좌석 별점
    private String viewTime;         // 관람 시간
    private String screenNumber;     // 상영관 번호
    private String screenType;       // 상영관 타입(2D,3D,IMAX 등)
    private String seatRow;          // 좌석 행
    private String seatCol;          // 좌석 열
    private long cinemaId;           // 영화관 ID
    private String cinemaName;       // 영화관 이름
    private String cinemaAddress;    // 영화관 주소
    private String brandName;        // 브랜드명 (CGV/롯데/메가박스/기타)
    private String regionName;       // 지역명
}
