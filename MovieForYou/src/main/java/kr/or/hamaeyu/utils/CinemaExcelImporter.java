package kr.or.hamaeyu.utils;

import java.io.File;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

public class CinemaExcelImporter {

    public static void main(String[] args) {
        String excelPath = "C:/data/KOBIS_영화상영관정보_2025-10-16.xls"; // 🔹 엑셀 파일 경로
        importCinemas(excelPath);
    }

    public static void importCinemas(String filePath) {
        try (Connection conn = ConnectionPoolHelper.getConnection()) {

            File file = new File(filePath);
            if (!file.exists()) {
                System.err.println("❌ 엑셀 파일을 찾을 수 없습니다: " + file.getAbsolutePath());
                return;
            }

            Workbook workbook;
            if (filePath.endsWith(".xlsx"))
                workbook = new XSSFWorkbook(new FileInputStream(file));
            else
                workbook = new HSSFWorkbook(new FileInputStream(file));

            Sheet sheet = workbook.getSheetAt(0);
            int rowCount = 0;

            String sql = """
                INSERT INTO cinema (id, cinema_name, latitude, longitude, cinema_address, region_id, cinema_type_id, cinema_brand_id)
                VALUES (cinema_seq.NEXTVAL, ?, ?, ?, ?, ?, ?, ?)
            """;

            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                conn.setAutoCommit(false);

                for (Row row : sheet) {
                    if (row.getRowNum() == 0) continue; // 헤더 스킵
                    Cell nameCell = row.getCell(0);
                    if (nameCell == null) continue;

                    String cinemaName = nameCell.getStringCellValue().trim();
                    if (cinemaName.isEmpty()) continue;

                    double latitude = getNumeric(row, 1);
                    double longitude = getNumeric(row, 2);
                    String address = getString(row, 3);
                    int regionId = (int) getNumeric(row, 4);
                    int typeId = classifyType(getString(row, 5)); // 2D,3D,IMAX 구분
                    int brandId = classifyBrand(cinemaName);      // CGV/롯데/메가박스/기타

                    ps.setString(1, cinemaName);
                    ps.setDouble(2, latitude);
                    ps.setDouble(3, longitude);
                    ps.setString(4, address);
                    ps.setInt(5, regionId);
                    ps.setInt(6, typeId);
                    ps.setInt(7, brandId);
                    ps.addBatch();

                    if (++rowCount % 100 == 0) ps.executeBatch();
                }

                ps.executeBatch();
                conn.commit();
                System.out.println("✅ 총 " + rowCount + "개의 영화관 데이터가 등록되었습니다.");
            }

            workbook.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static double getNumeric(Row r, int c) {
        try { return r.getCell(c).getNumericCellValue(); } catch (Exception e) { return 0; }
    }

    private static String getString(Row r, int c) {
        try {
            r.getCell(c).setCellType(CellType.STRING);
            return r.getCell(c).getStringCellValue().trim();
        } catch (Exception e) { return ""; }
    }

    private static int classifyBrand(String name) {
        name = name.toLowerCase();
        if (name.contains("cgv")) return 1;
        if (name.contains("롯데시네마")) return 2;
        if (name.contains("메가박스")) return 3;
        if (name.contains("자동차극장")) return 0; // 제외
        return 4; // 기타
    }

    private static int classifyType(String text) {
        if (text == null) return 1;
        text = text.toLowerCase();
        if (text.contains("imax")) return 5;
        if (text.contains("4d")) return 4;
        if (text.contains("3d")) return 3;
        return 2;
    }
}
