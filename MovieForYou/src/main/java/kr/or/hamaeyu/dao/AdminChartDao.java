package kr.or.hamaeyu.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class AdminChartDao {

    private static final String SQL = """
	        SELECT 
			    pt.id AS post_type_id,
			    pt.TYPE_NAME AS post_type_name,
			    COUNT(p.id) AS today_count
			FROM post p
			JOIN post_type pt ON p.type_id = pt.id
			WHERE TRUNC(p.created_at) = TRUNC(SYSDATE)
			  AND pt.id IN (1, 2, 3)
			GROUP BY pt.id, pt.TYPE_NAME
			ORDER BY pt.id
        """;

    public Map<String, Integer> getTodayPostCounts() {
        Map<String, Integer> result = new HashMap<>();
        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(SQL);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int typeId = rs.getInt("post_type_id");
                int count = rs.getInt("today_count");

                switch (typeId) {
                    case 1 -> result.put("free", count);
                    case 2 -> result.put("movieReview", count);
                    case 3 -> result.put("cinemaReview", count);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}

