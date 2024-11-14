package com.shopme.admin.util;

import com.lowagie.text.Font;
import com.lowagie.text.*;
import com.lowagie.text.pdf.BaseFont;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;

import java.awt.*;
import java.io.IOException;
import java.util.List;

public class UserPdfExporter extends AbstractExporter {


    public void export(List<User> userList, HttpServletResponse response) throws DocumentException, IOException {
        super.setResponseHeader("application", response, ".pdf", "users_");

        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());

        document.open();

        Font font = new Font(getBaseFont(), 16, Font.BOLD);
        font.setColor(Color.RED);

        Paragraph paragraph = new Paragraph("Danh sách người dùng", font);
        paragraph.setAlignment(Paragraph.ALIGN_CENTER);

        document.add(paragraph);

        PdfPTable table = new PdfPTable(6);
        table.setWidthPercentage(100f);
        table.setSpacingBefore(10);
        table.setWidths(new float[]{1.5f, 3.5f, 3.0f, 3.0f, 3.0f, 1.5f});

        writeTableHeader(table);
        writeDataLines(userList, table);

        document.add(table);

        document.close();
    }

    private BaseFont getBaseFont() throws IOException {
        return BaseFont.createFont("static/webfonts/Arial.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    }

    private void writeDataLines(List<User> userList, PdfPTable table) throws IOException {
        Font font = new Font(getBaseFont());

        for (User user : userList) {
            table.addCell(new Phrase(String.valueOf(user.getId()), font));
            table.addCell(new Phrase(user.getEmail(), font));
            table.addCell(new Phrase(user.getLastName(), font));
            table.addCell(new Phrase(user.getFirstName(), font));
            table.addCell(new Phrase(user.getRoles().toString(), font));
            table.addCell(new Phrase(String.valueOf(user.isEnabled()), font));
        }
    }

    private void writeTableHeader(PdfPTable table) throws IOException {
        PdfPCell cell = new PdfPCell();
        cell.setPadding(5);

        Font font = new Font(getBaseFont());
        font.setColor(Color.RED);

        cell.setPhrase(new Phrase("ID", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("E-mail", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Họ tên đệm", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Tên", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Vai trò", font));
        table.addCell(cell);

        cell.setPhrase(new Phrase("Đã kích hoạt", font));
        table.addCell(cell);
    }
}
