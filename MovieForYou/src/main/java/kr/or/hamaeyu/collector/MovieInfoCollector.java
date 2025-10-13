package kr.or.hamaeyu.collector;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.*;

import kr.or.hamaeyu.utils.ConnectionPoolHelper;
import lombok.ToString;
//import kobis.dao.DBUtil;
@ToString
public class MovieInfoCollector {
    private static final String API_KEY = "193de360-80dd-440b-87f3-2fcb029cf75c";

    static class DailyBoxOffice {
        String MOVIE_TITLE, DIRECTOR, RATING, RELEASE_DATE, SYNOPSIS, TRAILER_URL;
        int RUNTIME, COUNTRY_ID, GENRE_ID;
    }

    public static void collectDailyBoxOffice(String targetDt, String numOfRows) {
        try {
            String apiUrl = "https://api.kcisa.kr/openapi/service/rest/meta/KFAmovi"
                    + "?serviceKey=" + API_KEY + "&numOfRows=" + numOfRows;

            // 1️⃣ API 호출
            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
            StringBuilder result = new StringBuilder();
            String line;

            while ((line = br.readLine()) != null) result.append(line);
            br.close();

            // ✅ XML → JSON 변환
            JSONObject xmlJson = XML.toJSONObject(result.toString());

            // ✅ JSON 문자열을 GSON 객체로 변환
            JsonElement jsonElement = JsonParser.parseString(xmlJson.toString());
            JsonObject json = jsonElement.getAsJsonObject();
            
            System.out.println(json.toString());

            // ✅ 여기서부터 기존 로직 유지
            JsonArray list = json
                .getAsJsonObject("response")
                .getAsJsonObject("body")
                .getAsJsonObject("items")
                .getAsJsonArray("item"); 
	         
            // 3️⃣ DB 저장
            Connection con =  ConnectionPoolHelper.getConnection();
            String sql = """
            	    INSERT INTO movie(
            	        MOVIE_TITLE,
            	        DIRECTOR,
            	        RATING,
            	        RELEASE_DATE,
            	        RUNTIME,
            	        SYNOPSIS,
            	        TRAILER_URL,
            	        COUNTRY_ID,
            	        GENRE_ID
            	    )
            	    VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            	""";

            	PreparedStatement ps = con.prepareStatement(sql);

            	for (JsonElement e : list) {
            	    JsonObject item = e.getAsJsonObject();

            	    // 1️⃣ 영화 제목
            	    String title = item.has("title") && !item.get("title").getAsString().isEmpty()
            	        ? item.get("title").getAsString()
            	        : "미상";

            	 // 2️⃣ 감독 (person은 문자열이거나 배열일 수 있음, 단 감독은 1명만 저장)
            	    String director = "미상";
            	    if (item.has("person") && !item.get("person").isJsonNull()) {
            	        JsonElement personElem = item.get("person");
            	        if (personElem.isJsonArray()) {
            	            JsonArray arr = personElem.getAsJsonArray();
            	            if (arr.size() > 0) {
            	                director = arr.get(0).getAsString();
            	            }
            	        } else if (personElem.isJsonPrimitive()) {
            	            director = personElem.getAsString();
            	        }
            	    }


            	    // 3️⃣ 평점 (기본값 0)
            	    String rating = "0";

            	    // 4️⃣ 개봉일 (regDate는 '2018-02-02 17:29:14' 형태)
            	    java.sql.Date releaseDate;
            	    if (item.has("regDate") && !item.get("regDate").getAsString().isEmpty()) {
            	        String dateStr = item.get("regDate").getAsString().split(" ")[0]; // 날짜만 추출
            	        releaseDate = java.sql.Date.valueOf(dateStr);
            	    } else {
            	        releaseDate = java.sql.Date.valueOf("1900-01-01");
            	    }

            	    // 5️⃣ 상영시간
            	    int runtime = 0;
            	    if (item.has("extent") && !item.get("extent").getAsString().isEmpty()) {
            	        try {
            	            runtime = Integer.parseInt(item.get("extent").getAsString());
            	        } catch (NumberFormatException ex) {
            	            runtime = 0;
            	        }
            	    }

            	    // 6️⃣ 시놉시스
            	    String synopsis = item.has("description") && !item.get("description").isJsonNull()
            	        ? item.get("description").getAsString()
            	        : null;

            	    // 7️⃣ 나머지 컬럼들 (임시 null/0)
            	    String trailerUrl = null;
            	    int countryId = 0;
            	    int genreId = 0;

            	    // 8️⃣ PreparedStatement 세팅
            	    ps.setString(1, title);
            	    ps.setString(2, director);
            	    ps.setString(3, rating);
            	    ps.setDate(4, releaseDate);
            	    ps.setInt(5, runtime);
            	    ps.setString(6, synopsis);
            	    ps.setString(7, trailerUrl);
            	    ps.setInt(8, countryId);
            	    ps.setInt(9, genreId);

            	    ps.addBatch();
            	}



            ps.executeBatch();
            con.close();
            System.out.println("✅ " + targetDt + " 일별 박스오피스 저장 완료");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
