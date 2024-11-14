package com.shopme.admin.util;

import com.shopme.common.entity.Category;
import jakarta.servlet.http.HttpServletResponse;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class CategoryCsvExporter extends AbstractExporter {
    public void export(List<Category> categories, HttpServletResponse response) throws IOException {
        super.setResponseHeader("text/csv; charset=UTF-8", response, ".csv", "categories_");

        ICsvBeanWriter csvBeanWriter = new CsvBeanWriter(new OutputStreamWriter(response.getOutputStream(), StandardCharsets.UTF_8),
                CsvPreference.STANDARD_PREFERENCE);

        String[] csvHeader = {"ID", "Tên danh mục"};
        String[] fieldMapping = {"id", "name"};

        csvBeanWriter.writeHeader(csvHeader);
        for (Category category : categories) {
            category.setName(category.getName().replace("--", "  "));
            csvBeanWriter.write(category, fieldMapping);
        }

        csvBeanWriter.close();
    }
}
