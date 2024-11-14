package com.shopme.admin.util;

import com.shopme.common.entity.User;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.usermodel.*;

import java.io.IOException;
import java.util.List;

public class UserExcelExporter extends AbstractExporter {
    private XSSFWorkbook workbook;
    private XSSFSheet sheet;

    public UserExcelExporter() {
        workbook = new XSSFWorkbook();
    }

    private void writeHeaderLine() {
        sheet = workbook.createSheet("Người dùng");
        XSSFRow row = sheet.createRow(0);

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setBold(true);
        font.setFontHeight(16);
        cellStyle.setFont(font);

        createCell(row, 0, "ID", cellStyle);
        createCell(row, 1, "E-mail", cellStyle);
        createCell(row, 2, "Họ tên đệm", cellStyle);
        createCell(row, 3, "Tên", cellStyle);
        createCell(row, 4, "Vai trò", cellStyle);
        createCell(row, 5, "Đã kích hoạt", cellStyle);
    }

    private void writeDataLines(List<User> userList) {
        int rowIndex = 1;

        XSSFCellStyle cellStyle = workbook.createCellStyle();
        XSSFFont font = workbook.createFont();
        font.setFontHeight(14);
        cellStyle.setFont(font);

        for (User user : userList) {
            XSSFRow row = sheet.createRow(rowIndex++);

            createCell(row, 0, user.getId(), cellStyle);
            createCell(row, 1, user.getEmail(), cellStyle);
            createCell(row, 2, user.getLastName(), cellStyle);
            createCell(row, 3, user.getFirstName(), cellStyle);
            createCell(row, 4, user.getRoles().toString(), cellStyle);
            createCell(row, 5, user.isEnabled(), cellStyle);
        }
    }

    private void createCell(XSSFRow row, int columnIndex, Object value, CellStyle style) {
        XSSFCell cell = row.createCell(columnIndex);
        sheet.autoSizeColumn(columnIndex);

        if (value instanceof Integer) {
            cell.setCellValue((Integer) value);
        } else if (value instanceof Boolean) {
            cell.setCellValue((Boolean) value);
        } else {
            cell.setCellValue((String) value);
        }

        cell.setCellStyle(style);

    }

    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader("application/octet-stream", response, ".xlsx", "users_");

        writeHeaderLine();
        writeDataLines(userList);

        ServletOutputStream outputStream = response.getOutputStream();

        workbook.write(outputStream);
        workbook.close();
    }
}
