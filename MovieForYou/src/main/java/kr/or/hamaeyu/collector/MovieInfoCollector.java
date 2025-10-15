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

import kr.or.hamaeyu.dto.MovieTest;
import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class MovieInfoCollector {

    private static final String 문화공공데이터광장_API_KEY = "193de360-80dd-440b-87f3-2fcb029cf75c";

    // ✅ 안전하게 JSON에서 문자열 추출하는 유틸 메서드
    private static String safeGet(JsonObject obj, String key) {
        return obj.has(key) && !obj.get(key).isJsonNull()
                ? obj.get(key).getAsString()
                : null;
    }

    public static int collectDailyBoxOffice(String targetDt, String numOfRows) {
        Connection con = null;
        PreparedStatement pstmt = null;
        int totalInserted = 0;

        try {
            // ✅ 1️⃣ API 호출
            String apiUrl = "https://api.kcisa.kr/openapi/service/rest/meta/KFAmovi"
                    + "?serviceKey=" + 문화공공데이터광장_API_KEY + "&numOfRows=" + numOfRows;

            URL url = new URL(apiUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(
                new InputStreamReader(conn.getInputStream(), "UTF-8")
            );

            StringBuilder result = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) result.append(line);
            br.close();

            // ✅ XML → JSON 변환
            JSONObject xmlJson = XML.toJSONObject(result.toString());
            JsonElement jsonElement = JsonParser.parseString(xmlJson.toString());
            JsonObject json = jsonElement.getAsJsonObject();

            // ✅ 2️⃣ JSON 파싱
            JsonArray list = json
                .getAsJsonObject("response")
                .getAsJsonObject("body")
                .getAsJsonObject("items")
                .getAsJsonArray("item");

            // ✅ 3️⃣ DB 연결
            con = ConnectionPoolHelper.getConnection();
            String sql = """
                INSERT INTO MOVIE_TEST (
                    ID, 
                    UCI, TITLE, ALTERNATIVE_TITLE, SUBJECT_KEYWORD, SUBJECT_CATEGORY,
                    DESCRIPTION, CREATOR, CONTRIBUTOR, PERSON, LANGUAGE,
                    SPATIAL_COVERAGE, TEMPORAL, EXTENT, REG_DATE,
                    SOURCE_TITLE, RIGHTS, COPYRIGHT_OTHERS, COLLECTION_DB
                ) VALUES (
                    SEQ_MOVIE_TEST_ID.NEXTVAL,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, ?, ?,
                    ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'),
                    ?, ?, ?, ?
                )
            """;

            pstmt = con.prepareStatement(sql);

            // ✅ 4️⃣ 반복문: JSON 배열 → DTO → DB 저장
            for (JsonElement e : list) {
                JsonObject item = e.getAsJsonObject();
                MovieTest dto = new MovieTest();

                dto.setUci(safeGet(item, "uci"));
                dto.setTitle(safeGet(item, "title"));
                dto.setAlternativeTitle(safeGet(item, "alternativeTitle"));
                dto.setSubjectKeyword(safeGet(item, "subjectKeyword"));
                dto.setSubjectCategory(safeGet(item, "subjectCategory"));
                dto.setDescription(safeGet(item, "description"));
                dto.setCreator(safeGet(item, "creator"));
                dto.setContributor(safeGet(item, "contributor"));
                dto.setPerson(safeGet(item, "person"));
                dto.setLanguage(safeGet(item, "language"));
                dto.setSpatialCoverage(safeGet(item, "spatialCoverage"));
                dto.setTemporal(safeGet(item, "temporal"));

                // extent는 비거나 null일 수 있음 → 0 처리
                if (item.has("extent") && !item.get("extent").isJsonNull()
                        && !item.get("extent").getAsString().isEmpty()) {
                    try {
                        dto.setExtent(Integer.parseInt(item.get("extent").getAsString()));
                    } catch (NumberFormatException ex) {
                        dto.setExtent(0);
                    }
                } else {
                    dto.setExtent(0);
                }

                dto.setRegDate(safeGet(item, "regDate"));
                dto.setSourceTitle(safeGet(item, "sourceTitle"));
                dto.setRights(safeGet(item, "rights"));
                dto.setCopyrightOthers(safeGet(item, "copyrightOthers"));
                dto.setCollectionDb(safeGet(item, "collectionDb"));

                // ✅ DB 바인딩
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
                pstmt.setString(14, dto.getRegDate());
                pstmt.setString(15, dto.getSourceTitle());
                pstmt.setString(16, dto.getRights());
                pstmt.setString(17, dto.getCopyrightOthers());
                pstmt.setString(18, dto.getCollectionDb());

                totalInserted += pstmt.executeUpdate();

                System.out.println("✅ Inserted: " + dto.getTitle() + " (" + dto.getUci() + ")");
            }

            System.out.println("🎬 총 " + totalInserted + "건 저장 완료");
            return totalInserted;

        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        } finally {
            try { if (pstmt != null) pstmt.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }
    
    private static int getTestData2() {
    	
		return 0;
    }
}
