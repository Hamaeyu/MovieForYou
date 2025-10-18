<%@ page import="java.sql.Connection" %>
<%@ page import="kr.or.hamaeyu.utils.ConnectionPoolHelper" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>HikariCP DB 연결 테스트</title>
</head>
<body>
<h2>DB 연결 테스트</h2>
<%
    Connection conn = null;
    try {
        conn = ConnectionPoolHelper.getConnection();
        if (conn != null && !conn.isClosed()) {
            out.println("<p style='color:green;'>DB 연결 성공! Connection  닫혔니?: " + conn.isClosed() + "</p>");
        } else {
            out.println("<p style='color:red;'>DB 연결 실패!</p>");
        }
    } catch (Exception e) {
        out.println("<p style='color:red;'>예외 발생: " + e.getMessage() + "</p>");

        // JspWriter를 PrintWriter로 변환 후 전달
        e.printStackTrace(new java.io.PrintWriter(out));
    } finally {
        if (conn != null) {
            try {
                conn.close();
                out.println("<p>커넥션 반환 완료. Connection 닫혔니?: " + conn.isClosed() + "</p>");
            } catch (Exception e) {
                e.printStackTrace(new java.io.PrintWriter(out));
            }
        }
    }
%>
</body>
</html>
