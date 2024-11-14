package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.ProductRepository;
import com.shopme.admin.service.ProductService;
import com.shopme.common.entity.Product;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    public static final int PRODUCTS_PER_PAGE = 6;

    @Autowired
    private ProductRepository repo;

    @Override
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    @Override
    @Transactional
    public Product saveProduct(Product product) {
        if (product.getId() == null) {
            product.setCreatedTime(new Date());
        }

        if (product.getAlias() == null || product.getAlias().isEmpty()) {
            product.setAlias(generateAlias(product.getName()));
        } else {
            product.setAlias(generateAlias(product.getAlias()));
        }
        product.setUpdatedTime(new Date());

        return repo.save(product);
    }

    @Override
    public Product findProductById(Integer id) throws ResourceNotFoundException {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy sản phẩm có ID:  " + id));
    }

    @Override
    public boolean isUniqueProduct(Integer id, String name) {
        boolean isCreatingNew = id == null;
        Product productByName = repo.findByName(name);

        if (productByName == null) {
            return true;
        }

        if (isCreatingNew) {
            return false;
        }

        return productByName.getId().equals(id);
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Integer id, boolean enabled) {
        repo.updateEnabledStatus(id, enabled);
    }

    @Override
    @Transactional
    public void deleteProductById(Integer id) {
        repo.deleteById(id);
    }

    @Override
    public Page<Product> listByPage(Integer pageNum, PagingAndSortingHelper helper, Integer categoryId) {
        String sortField = helper.getSortField();
        String sortDir = helper.getSortDir();
        String keyword = helper.getKeyword();

        Sort sort;
        if (sortField.equals("category")) {
            sort = Sort.by("category.name");
        } else if (sortField.equals("brand")) {
            sort = Sort.by("brand.name");
        } else {
            sort = Sort.by(sortField);
        }

        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, PRODUCTS_PER_PAGE, sort);
        if (keyword != null && !keyword.isEmpty()) {
            if (categoryId != null && categoryId > 0) {
                String categoryMatch = "-" + categoryId + "-";
                return repo.searchByCategory(categoryId, categoryMatch, keyword, pageable);
            }

            return repo.search(keyword, pageable);
        }

        if (categoryId != null && categoryId > 0) {
            String categoryMatch = "-" + categoryId + "-";
            return repo.findAllByCategory(categoryId, categoryMatch, pageable);
        }

        return repo.findAll(pageable);
    }

    @Override
    public void saveProductPrice(Product product) {
        Product productInDb = repo.findById(product.getId()).get();

        productInDb.setCost(product.getCost());
        productInDb.setPrice(product.getPrice());
        productInDb.setDiscountPercent(product.getDiscountPercent());
        productInDb.setUpdatedTime(new Date());

        repo.save(productInDb);
    }

    private String generateAlias(String name) {
        // Chuyển sang chữ thường
        String alias = name.toLowerCase();

        // Thay thế dấu tiếng Việt bằng ký tự không dấu
        alias = alias.replaceAll("[áàảãạâấầẩẫậăắằẳẵặ]", "a")
                .replaceAll("[éèẻẽẹêếềểễệ]", "e")
                .replaceAll("[íìỉĩị]", "i")
                .replaceAll("[óòỏõọôốồổỗộơớờởỡợ]", "o")
                .replaceAll("[úùủũụưứừửữự]", "u")
                .replaceAll("[ýỳỷỹỵ]", "y")
                .replaceAll("[đ]", "d");

        // Thay thế khoảng trắng bằng dấu gạch ngangg
        alias = alias.replaceAll("\\s+", "-");

        return alias;
    }
}
