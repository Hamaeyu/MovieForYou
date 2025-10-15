package kr.or.hamaeyu.collector;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.json.JSONObject;
import org.json.XML;
import com.google.gson.*;

import kr.or.hamaeyu.utils.ConnectionPoolHelper;

public class PosterInfoCollector {
	private static final String POSTER_API = "https://api.kcisa.kr/openapi/service/rest/meta/KFApost";
	private static final String BOX_OFFICE_API = "https://api.kcisa.kr/openapi/service/rest/meta5/getKFCC0502";
	private static final String 포스터_API_KEY = "41471d8a-37e1-43d1-bc2b-dd59513c8c2f";
	private static final String 박스오피스_API_KEY = "b74347c2-f649-4fed-b024-829dfd864e94";

    public static String fetchPosterUrlByUci(String targetUci) throws Exception {
        String encodedUci = URLEncoder.encode(targetUci, StandardCharsets.UTF_8);
        String apiUrl = "https://api.kcisa.kr/openapi/service/rest/meta/KFApost"
                + "?serviceKey=" + 포스터_API_KEY
                + "&numOfRows=1"
                + "&uci=" + encodedUci;

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream stream;
        int code = conn.getResponseCode();
        if (code >= 200 && code < 300) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
            System.err.println("❌ API 요청 실패: 응답 코드 " + code);
        }

        if (stream == null) {
            throw new IOException("서버 응답 스트림이 없습니다. URL 또는 API Key를 확인하세요.");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        conn.disconnect();

        System.out.println("🔍 응답 원문:\n" + sb);

        JSONObject xmlJson = XML.toJSONObject(sb.toString());
        JsonObject json = JsonParser.parseString(xmlJson.toString()).getAsJsonObject();

        JsonObject body = json.getAsJsonObject("response").getAsJsonObject("body");
        JsonObject items = body.getAsJsonObject("items");
        JsonObject item = items.getAsJsonObject("item");

        if (item.has("posterUrl")) {
            return item.get("posterUrl").getAsString();
        } else if (item.has("posterFileUrl")) {
            return item.get("posterFileUrl").getAsString();
        } else {
            System.out.println("포스터 항목 전체: " + item);
            return null;
        }
    }
    
    public static void fetchBoxOffice() throws Exception {
        String apiUrl = BOX_OFFICE_API
                + "?serviceKey=" + 박스오피스_API_KEY
                + "&numOfRows=10"; // 여러 개 받게끔

        System.out.println("📡 요청 URL: " + apiUrl);

        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");

        InputStream stream;
        int code = conn.getResponseCode();
        if (code >= 200 && code < 300) {
            stream = conn.getInputStream();
        } else {
            stream = conn.getErrorStream();
            System.err.println("❌ API 요청 실패: 응답 코드 " + code);
        }

        if (stream == null) {
            throw new IOException("서버 응답 스트림이 없습니다. URL 또는 API Key를 확인하세요.");
        }

        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();
        conn.disconnect();

        System.out.println("🔍 응답 원문:\n" + sb);

        // XML → JSON 변환
        JSONObject xmlJson = XML.toJSONObject(sb.toString());
        JsonObject json = JsonParser.parseString(xmlJson.toString()).getAsJsonObject();

        JsonObject body = json.getAsJsonObject("response").getAsJsonObject("body");
        JsonObject items = body.getAsJsonObject("items");
        JsonElement itemElement = items.get("item");

        // ✅ 배열/단일객체 구분
        if (itemElement.isJsonArray()) {
            JsonArray itemArray = itemElement.getAsJsonArray();
            for (JsonElement elem : itemArray) {
                JsonObject item = elem.getAsJsonObject();
                insertBoxOfficeData(item);
            }
        } else if (itemElement.isJsonObject()) {
            JsonObject item = itemElement.getAsJsonObject();
            insertBoxOfficeData(item);
        } else {
            System.err.println("⚠️ item 구조가 예상과 다릅니다: " + itemElement);
        }
    }

    // ✅ DB 저장 함수
    private static void insertBoxOfficeData(JsonObject item) {
        String sql = "INSERT INTO BOX_OFFICE_DATA (" +
                "ID, SUBJECT_KEYWORD, EXTENT, CREATOR, REFERENCE_ID, ALT_TITLE, REG_DATE, DESCRIPTION, " +
                "SOURCE_TITLE, LANGUAGE, TITLE, SPATIAL_COVERAGE, URL, SUBJECT_CATEGORY, CONTRIBUTOR, " +
                "PERSON, RIGHTS, COPYRIGHT_OTHERS, COLLECTION_DB, TEMPORAL) " +
                "VALUES (SEQ_BOX_OFFICE_ID.NEXTVAL, ?, ?, ?, ?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'), ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = ConnectionPoolHelper.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1,  item.has("subjectKeyword") ? item.get("subjectKeyword").getAsString() : null);
            ps.setString(2,  item.has("extent") ? item.get("extent").getAsString() : null);
            ps.setString(3,  item.has("creator") ? item.get("creator").getAsString() : null);
            ps.setString(4,  item.has("referenceIdentifier") ? item.get("referenceIdentifier").getAsString() : null);
            ps.setString(5,  item.has("alternativeTitle") ? item.get("alternativeTitle").getAsString() : null);
            ps.setString(6,  item.has("regDate") ? item.get("regDate").getAsString() : null);
            ps.setString(7,  item.has("description") ? item.get("description").getAsString() : null);
            ps.setString(8,  item.has("sourceTitle") ? item.get("sourceTitle").getAsString() : null);
            ps.setString(9,  item.has("language") ? item.get("language").getAsString() : null);
            ps.setString(10, item.has("title") ? item.get("title").getAsString() : null);
            ps.setString(11, item.has("spatialCoverage") ? item.get("spatialCoverage").getAsString() : null);
            ps.setString(12, item.has("url") ? item.get("url").getAsString() : null);
            ps.setString(13, item.has("subjectCategory") ? item.get("subjectCategory").getAsString() : null);
            ps.setString(14, item.has("contributor") ? item.get("contributor").getAsString() : null);
            ps.setString(15, item.has("person") ? item.get("person").getAsString() : null);
            ps.setString(16, item.has("rights") ? item.get("rights").getAsString() : null);
            ps.setString(17, item.has("copyrightOthers") ? item.get("copyrightOthers").getAsString() : null);
            ps.setString(18, item.has("collectionDb") ? item.get("collectionDb").getAsString() : null);
            ps.setString(19, item.has("temporal") ? item.get("temporal").getAsString() : null);

            int rows = ps.executeUpdate();
            System.out.println("✅ 저장 성공: " + rows + "건 (" + item.get("title").getAsString() + ")");
        } catch (Exception e) {
            System.err.println("⚠️ DB 저장 중 오류: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        try {
        	fetchBoxOffice();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
