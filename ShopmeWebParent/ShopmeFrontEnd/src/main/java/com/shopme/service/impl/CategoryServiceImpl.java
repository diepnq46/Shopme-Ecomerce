package com.shopme.service.impl;

import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CategoryRepository;
import com.shopme.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private CategoryRepository repo;

    @Override
    public List<Category> listNoChildrenCategories() {
        List<Category> enabledCategories = repo.findEnabledCategories();

        List<Category> noChildrenCategories = new ArrayList<Category>();
        for (Category category : enabledCategories) {
            if (category.getChildren() == null || category.getChildren().isEmpty()) {
                noChildrenCategories.add(category);
            }
        }

        return noChildrenCategories;
    }

    @Override
    public Category getCategory(String alias) throws ResourceNotFoundException {
        Category category = repo.findByAliasEnabled(alias);
        if (category == null) {
            throw new ResourceNotFoundException("Không tìm thấy danh mục!");
        }

        return category;
    }

    @Override
    public List<Category> getParentCategories(Category child) {
        Category parent = child.getParent();
        List<Category> listParentCategories = new ArrayList<>();

        while (parent != null) {
            listParentCategories.addFirst(parent);

            parent = parent.getParent();
        }

        listParentCategories.add(child);

        return listParentCategories;
    }
}
