package com.shopme.admin.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Brand;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface BrandService {
    Page<Brand> getBrandsByPage(Integer pageNum, PagingAndSortingHelper helper);

    List<Brand> getAllBrands();

    Brand save(Brand brand);

    Brand findBrandById(Integer id) throws ResourceNotFoundException;

    void deleteBrandById(Integer id);

    boolean isUniqueBrand(Integer id, String name);

}
