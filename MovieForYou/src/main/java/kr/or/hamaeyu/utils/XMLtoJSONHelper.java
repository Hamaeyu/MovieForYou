package kr.or.hamaeyu.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.JSONObject;
import org.json.XML;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 * ApiHelper
 * XML 기반 공공데이터(Open API) 응답을 JSON 객체로 변환하는 헬퍼 클래스
 *
 * 사용 예시:
 * JsonObject json = ApiHelper.getJsonResponse("https://api.kcisa.kr/openapi/service/rest/meta/KFCmovi?serviceKey=키&pageNo=1");
 */
public class XMLtoJSONHelper {

    // 기본 요청 타임아웃 (밀리초)
    private static final int TIMEOUT = 20000;

    /**
     * 주어진 API URL(XML 응답)을 호출하여 JSON 객체로 반환
     */
    public static JsonElement getJsonResponse(String apiUrl) throws Exception {
        // 1️⃣ API 호출
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        // 2️⃣ XML → JSONObject 변환
        JSONObject xmlToJson = XML.toJSONObject(sb.toString());

        // 3️⃣ Gson 호환 JsonElement로 변환
        return JsonParser.parseString(xmlToJson.toString());
    }

    /**
     * XML 원문을 그대로 문자열로 받기 (디버깅용)
     */
    public static String getRawXml(String apiUrl) throws Exception {
        URL url = new URL(apiUrl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(TIMEOUT);
        conn.setReadTimeout(TIMEOUT);

        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) sb.append(line);
        br.close();

        return sb.toString();
    }
}
