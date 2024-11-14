package com.shopme.admin.service.impl;

import com.shopme.admin.repository.CategoryRepository;
import com.shopme.admin.response.PageCategoryResponse;
import com.shopme.admin.service.CategoryService;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryServiceImpl implements CategoryService {
    public static final int ROOT_CATEGORY_PER_PAGE = 4;

    @Autowired
    private CategoryRepository repo;

    @Override
    public PageCategoryResponse getAllCategories(int pageNum, String sortDir, String keyword) {
        Sort sort = Sort.by("name");

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ROOT_CATEGORY_PER_PAGE, sort);

        Page<Category> categoriesPage = null;
        if (keyword != null) {
            categoriesPage = repo.searchCategories(keyword, pageable);

            return new PageCategoryResponse(categoriesPage.getTotalPages(),
                    categoriesPage.getTotalElements(),
                    categoriesPage.getContent());
        }
        categoriesPage = repo.findRootCategories(pageable);
        List<Category> categories = categoriesPage.getContent();

        return new PageCategoryResponse(categoriesPage.getTotalPages(),
                categoriesPage.getTotalElements(),
                listHierarchicalCategories(categories, sortDir));
    }

    private List<Category> listHierarchicalCategories(List<Category> categories, String sortDir) {
        List<Category> hierarchicalCategories = new ArrayList<>();

        for (Category category : categories) {
            hierarchicalCategories.add(Category.copyFull(category));

            Set<Category> children = sortSubCategories(category.getChildren(), sortDir);

            for (Category child : children) {
                String name = "--" + child.getName();
                hierarchicalCategories.add(Category.copyFull(child, name));

                listSubHierarchicalCategories(hierarchicalCategories, child, 1, sortDir);
            }
        }

        return hierarchicalCategories;
    }

    private void listSubHierarchicalCategories(List<Category> hierarchicalCategories, Category child, int subLevel, String sortDir) {
        int newLevel = subLevel + 1;
        StringBuilder name = new StringBuilder();
        name.append("--".repeat(Math.max(0, newLevel)));

        Set<Category> children = sortSubCategories(child.getChildren(), sortDir);
        for (Category subChild : children) {
            hierarchicalCategories.add(Category.copyFull(subChild, name + subChild.getName()));

            listSubHierarchicalCategories(hierarchicalCategories, subChild, newLevel, sortDir);
        }
    }

    @Override
    public List<Category> getAllCategoriesInform() {
        List<Category> categoriesInForm = new ArrayList<>();
        Iterable<Category> categoriesInDb = repo.findRootCategories(Sort.by("name").ascending());

        for (Category cat : categoriesInDb) {
            if (cat.getParent() == null) {
                categoriesInForm.add(new Category(cat.getId(), cat.getName()));
                Set<Category> children = sortSubCategories(cat.getChildren());

                for (Category child : children) {
                    categoriesInForm.add(new Category(child.getId(), "--" + child.getName()));

                    listSubCategories(categoriesInForm, child, 1);
                }
            }
        }

        return categoriesInForm;
    }

    @Override
    @Transactional
    public Category save(Category category) {
        if (category.getPhotos().isEmpty()) {
            category.setPhotos(null);
        }

        Category parent = category.getParent();
        if (parent != null) {
            String allParentIds = parent.getAllParentIds() == null ? "-" : parent.getAllParentIds();
            allParentIds += parent.getId() + "-";
            category.setAllParentIds(allParentIds);
        }

        return repo.save(category);
    }

    @Override
    public Category findCategoryById(Integer id) throws ResourceNotFoundException {

        return repo.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy danh mục có ID: " + id)
        );
    }

    @Override
    public String checkUnique(Integer id, String name, String alias) {
        boolean isCreatingNew = id == null;
        Category categoryByName = repo.findByName(name);

        if (!isCreatingNew) {
            if (categoryByName != null && !categoryByName.getId().equals(id)) {
                return "DuplicateName";
            }

            Category categoryByAlias = repo.findByAlias(alias);
            if (categoryByAlias != null && !categoryByAlias.getId().equals(id)) {
                return "DuplicateAlias";
            }

            return "OK";
        }

        if (categoryByName != null) {
            return "DuplicateName";
        }

        Category categoryByAlias = repo.findByAlias(alias);
        if (categoryByAlias != null) {
            return "DuplicateAlias";
        }

        return "OK";
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    @Override
    public void deleteCategoryById(Integer id) {
        repo.deleteById(id);
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children) {
        return sortSubCategories(children, "asc");
    }

    private SortedSet<Category> sortSubCategories(Set<Category> children, String sortDir) {
        SortedSet<Category> sortedSet = new TreeSet<>(new Comparator<Category>() {
            @Override
            public int compare(Category c1, Category c2) {
                if (sortDir.equals("asc")) {
                    return c1.getName().compareTo(c2.getName());
                } else {
                    return c2.getName().compareTo(c1.getName());
                }
            }
        });

        sortedSet.addAll(children);
        return sortedSet;
    }

    private void listSubCategories(List<Category> categoriesInForm, Category child, int subLevel) {
        int newLevel = subLevel + 1;

        StringBuilder name = new StringBuilder();
        name.append("--".repeat(Math.max(0, newLevel)));

        Set<Category> children = sortSubCategories(child.getChildren());

        for (Category subChildren : children) {
            categoriesInForm.add(new Category(subChildren.getId(), name + subChildren.getName()));

            listSubCategories(categoriesInForm, subChildren, newLevel);
        }
    }
}
