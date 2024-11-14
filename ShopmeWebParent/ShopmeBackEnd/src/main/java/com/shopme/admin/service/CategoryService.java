package com.shopme.admin.service;

import com.shopme.admin.response.PageCategoryResponse;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {
    PageCategoryResponse getAllCategories(int pageNum, String sortDir, String keyword);

    List<Category> getAllCategoriesInform();

    Category save(Category category);

    Category findCategoryById(Integer id) throws ResourceNotFoundException;

    String checkUnique(Integer id, String name, String alias);

    void updateEnabledStatus(Integer id, boolean enabled);

    void deleteCategoryById(Integer id);
}
