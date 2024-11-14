package com.shopme.admin.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();

    Product saveProduct(Product product);

    Product findProductById(Integer id) throws ResourceNotFoundException;

    boolean isUniqueProduct(Integer id, String name);

    void updateEnabledStatus(Integer id, boolean enabled);

    void deleteProductById(Integer id);

    Page<Product> listByPage(Integer pageNum, PagingAndSortingHelper helper, Integer categoryId);

    void saveProductPrice(Product product);
}
