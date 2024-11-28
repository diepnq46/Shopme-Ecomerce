package com.shopme.repository;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    @Query("SELECT p FROM Product p WHERE p.enabled = true AND (p.category.id = ?1" +
            " OR p.category.allParentIds LIKE %?2%)" +
            " ORDER BY p.name ASC"
    )
    Page<Product> findByCategory(Integer categoryId, String categoryMatch, Pageable pageable);

    Optional<Product> findByAlias(String alias);

    @Query(value = "SELECT * FROM products p WHERE" +
            " enabled = true" +
            " AND (MATCH (name, short_description, full_description) AGAINST (?1))"
            , nativeQuery = true
    )
    Page<Product> search(String keyword, Pageable pageable);
}
