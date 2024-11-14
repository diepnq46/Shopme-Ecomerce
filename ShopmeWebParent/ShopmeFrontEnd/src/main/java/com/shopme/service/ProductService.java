package com.shopme.service;

import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface ProductService {
    Page<Product> getProductsByCategory(int pageNum, Integer categoryId);

    Product getProductByAlias(String alias) throws ResourceNotFoundException;

    Page<Product> searchProducts(int pageNum, String keyword);
}
