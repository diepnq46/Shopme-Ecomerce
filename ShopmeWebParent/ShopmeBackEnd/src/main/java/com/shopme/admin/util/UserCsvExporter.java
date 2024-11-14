package com.shopme.admin.util;

import com.shopme.common.entity.User;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class UserCsvExporter extends AbstractExporter {
    public void export(List<User> userList, HttpServletResponse response) throws IOException {
        super.setResponseHeader("text/csv; charset=UTF-8", response, ".csv", "users_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"ID", "E-mail", "Họ tên đệm", "Tên", "Vai trò", "Đã kích hoạt"};
        String[] fieldMapping = {"id", "email", "lastName", "firstName", "roles", "enabled"};

        csvBeanWriter.writeHeader(csvHeader);
        for (User user : userList) {
            csvBeanWriter.write(user, fieldMapping);
        }

        csvBeanWriter.close();
    }
}
