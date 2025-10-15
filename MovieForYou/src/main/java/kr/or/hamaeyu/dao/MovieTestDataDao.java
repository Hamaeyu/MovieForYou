package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import kr.or.hamaeyu.dto.MovieTest;

public class MovieTestDataDao {

    public static int insertMovieMetadata(Connection conn, MovieTest dto) throws SQLException {
        String sql = "INSERT INTO MOVIE_METADATA ("
                   + "UCI, TITLE, ALTERNATIVE_TITLE, SUBJECT_KEYWORD, SUBJECT_CATEGORY, "
                   + "DESCRIPTION, CREATOR, CONTRIBUTOR, PERSON, LANGUAGE, "
                   + "SPATIAL_COVERAGE, TEMPORAL, EXTENT, REG_DATE, "
                   + "SOURCE_TITLE, RIGHTS, COPYRIGHT_OTHERS, COLLECTION_DB"
                   + ") VALUES ("
                   + "?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?"
                   + ")";

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dto.getUci());
            pstmt.setString(2, dto.getTitle());
            pstmt.setString(3, dto.getAlternativeTitle());
            pstmt.setString(4, dto.getSubjectKeyword());
            pstmt.setString(5, dto.getSubjectCategory());
            pstmt.setString(6, dto.getDescription());
            pstmt.setString(7, dto.getCreator());
            pstmt.setString(8, dto.getContributor());
            pstmt.setString(9, dto.getPerson());
            pstmt.setString(10, dto.getLanguage());
            pstmt.setString(11, dto.getSpatialCoverage());
            pstmt.setString(12, dto.getTemporal());
            pstmt.setInt(13, dto.getExtent());
            pstmt.setString(14, dto.getRegDate());  // "2018-02-02 17:29:14" 형식 문자열
            pstmt.setString(15, dto.getSourceTitle());
            pstmt.setString(16, dto.getRights());
            pstmt.setString(17, dto.getCopyrightOthers());
            pstmt.setString(18, dto.getCollectionDb());

            return pstmt.executeUpdate(); // 성공 시 1 리턴
        }
    }
}

