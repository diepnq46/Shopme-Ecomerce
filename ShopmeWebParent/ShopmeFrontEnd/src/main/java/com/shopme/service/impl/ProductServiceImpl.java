package com.shopme.service.impl;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.ProductRepository;
import com.shopme.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ProductServiceImpl implements ProductService {
    public static final int PRODUCTS_PER_PAGE = 12;

    @Autowired
    private ProductRepository repo;

    @Override
    public Page<Product> getProductsByCategory(int pageNum, Integer categoryId) {
        String categoryMatch = "-" + categoryId + "-";
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return repo.findByCategory(categoryId, categoryMatch, pageable);
    }

    @Override
    public Product getProductByAlias(String alias) throws ResourceNotFoundException {
        return repo.findByAlias(alias).orElseThrow(
                () -> new ResourceNotFoundException("Không tìm thấy sản phẩm!")
        );
    }

    @Override
    public Page<Product> searchProducts(int pageNum, String keyword) {
        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE);

        return repo.search(keyword, pageable);
    }
}
