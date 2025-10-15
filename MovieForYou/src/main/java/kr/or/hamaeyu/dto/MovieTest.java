package kr.or.hamaeyu.dto;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor 
@AllArgsConstructor
@Builder @Getter @Setter @ToString
public class MovieTest {
    private String uci;
    private String title;
    private String alternativeTitle;
    private String subjectKeyword;
    private String subjectCategory;
    private String description;
    private String creator;
    private String contributor;
    private String person;
    private String language;
    private String spatialCoverage;
    private String temporal;
    private int extent;
    private String regDate;
    private String sourceTitle;
    private String rights;
    private String copyrightOthers;
    private String collectionDb;
}
