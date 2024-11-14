package com.shopme.admin.response;

import com.shopme.common.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageCategoryResponse {
    private int totalPages;
    private long totalElements;
    private List<Category> categories;
}
