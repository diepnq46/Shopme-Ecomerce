package com.shopme.admin.controller;

import com.shopme.admin.dto.CategoryDto;
import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/brands")
@RequiredArgsConstructor
public class BrandRestController {
    private final BrandService service;


    @PostMapping("/check-unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("name") String name) {
        return service.isUniqueBrand(id, name) ? "OK" : "Duplicated";
    }

    @GetMapping("/{id}/categories")
    public List<CategoryDto> getCategoriesByBrandId(@PathVariable Integer id) {
        try {
            List<CategoryDto> categoryDtoList = new ArrayList<>();

            Brand brand = service.findBrandById(id);
            Set<Category> categories = brand.getCategories();

            for (Category category : categories) {
                categoryDtoList.add(new CategoryDto(category));
            }

            return categoryDtoList;
        } catch (ResourceNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
