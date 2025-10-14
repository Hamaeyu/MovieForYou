package kr.or.hamaeyu.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import kr.or.hamaeyu.dto.Cinema;
import kr.or.hamaeyu.dto.CinemaFullInfo;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class AdminCinemaDao {

    public int insertTheater(Cinema dto) {
        int result = 0;
        String sql = "INSERT INTO CINEMA (ID, CINEMA_NAME, LATITUDE, LONGITUDE, CINEMA_ADDRESS, REGION_ID, CINEMA_TYPE_ID) " +
                     "VALUES (CINEMA_ID_SEQ.NEXTVAL, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, dto.getName());
            ps.setDouble(2, dto.getLat());
            ps.setDouble(3, dto.getLng());
            ps.setString(4, dto.getAddress());
            ps.setInt(5, dto.getRegion());
            ps.setInt(6, dto.getType());
            result = ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
    
    public List<CinemaFullInfo> getCinemaList() {
	    List<CinemaFullInfo> list = new ArrayList<>();
	    String sql = "select * from cinema c"
	    		+ "    left outer join region r on c.region_id = r.id"
	    		+ "    left outer join cinema_brand b on c.cinema_type_id = b.id";

	    try (Connection conn = ConnectionPoolHelper.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement(sql)) {

	        ResultSet rs = pstmt.executeQuery();
	        while (rs.next()) {
	        	CinemaFullInfo c = new CinemaFullInfo();
	            c.setTheaterId(rs.getInt("id"));
	            c.setName(rs.getString("cinema_name"));
	            c.setLat(rs.getDouble("latitude"));
	            c.setLng(rs.getDouble("longitude"));
	            c.setAddress(rs.getString("cinema_address"));
	            c.setRegion(rs.getString("region_name"));
	            c.setType(rs.getString("brand_name"));
	            list.add(c);
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return list;
	}

}
