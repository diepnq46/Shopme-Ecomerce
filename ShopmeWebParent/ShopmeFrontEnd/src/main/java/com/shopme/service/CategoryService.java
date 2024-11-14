package com.shopme.service;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;

import java.util.List;

public interface CategoryService {
    List<Category> listNoChildrenCategories();

    Category getCategory(String alias) throws ResourceNotFoundException;

    List<Category> getParentCategories(Category child);
}
