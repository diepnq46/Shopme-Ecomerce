package com.shopme.admin.repository;

import com.shopme.common.entity.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {
    Product findByName(String name);

    @Query("UPDATE Product p SET p.enabled = :enabled WHERE p.id = :id")
    @Modifying
    void updateEnabledStatus(@Param("id") Integer id, @Param("enabled") boolean enabled);

    @Query("SELECT p FROM Product p WHERE p.name LIKE %?1%" +
            " OR p.shortDescription LIKE %?1%" +
            " OR p.fullDescription LIKE %?1%" +
            " OR p.brand.name LIKE %?1%" +
            " OR p.category.name LIKE %?1%"
    )
    Page<Product> search(String keyword, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE" +
            " p.category.id = ?1" +
            " OR p.category.allParentIds LIKE %?2%"
    )
    Page<Product> findAllByCategory(Integer categoryId, String categoryMatch, Pageable pageable);


    @Query("SELECT p FROM Product p WHERE" +
            " (p.category.id = ?1" +
            " OR p.category.allParentIds LIKE %?2%)" +
            " AND (p.name LIKE %?3%" +
            " OR p.shortDescription LIKE %?3%" +
            " OR p.fullDescription LIKE %?3%" +
            " OR p.brand.name LIKE %?3%" +
            " OR p.category.name LIKE %?3%)"
    )
    Page<Product> searchByCategory(Integer categoryId, String categoryMatch, String keyword, Pageable pageable);
}
