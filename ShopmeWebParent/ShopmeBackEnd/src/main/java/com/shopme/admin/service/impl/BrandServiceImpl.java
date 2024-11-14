package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.BrandRepository;
import com.shopme.admin.service.BrandService;
import com.shopme.common.entity.Brand;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandServiceImpl implements BrandService {
    public static final int BRAND_PER_PAGE = 10;
    @Autowired
    private BrandRepository repo;

    @Override
    public Page<Brand> getBrandsByPage(Integer pageNum, PagingAndSortingHelper helper) {
        Sort sort = Sort.by("name");
        sort = helper.getSortDir().equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, BRAND_PER_PAGE, sort);
        if (helper.getKeyword() == null) {
            return repo.findAll(pageable);
        }

        return repo.searchBrand(helper.getKeyword(), pageable);
    }

    @Override
    public List<Brand> getAllBrands() {
        return repo.findAllWithIdAndName();
    }

    @Override
    @Transactional
    public Brand save(Brand brand) {
        if (brand.getLogo().isEmpty()) {
            brand.setLogo(null);
        }

        return repo.save(brand);
    }

    @Override
    public Brand findBrandById(Integer id) throws ResourceNotFoundException {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy nhãn hàng có ID: " + id));
    }

    @Override
    @Transactional
    public void deleteBrandById(Integer id) {
        repo.deleteById(id);
    }

    public boolean isUniqueBrand(Integer id, String name) {
        boolean isCreatingNew = id == null;
        Brand brandByName = repo.findByName(name);

        if (brandByName == null) {
            return true;
        }

        if (isCreatingNew) {
            return false;
        }

        return brandByName.getId().equals(id);
    }

}
