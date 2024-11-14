package com.shopme.admin.brand;

import com.shopme.admin.repository.BrandRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.HashSet;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class BrandRepositoryTest {
    @Autowired
    private BrandRepository repo;

    @Test
    public void testCreateNewBrand() {
        Brand brand = new Brand();
        brand.setName("Samsung");

        Category category_1 = new Category(24);
        Category category_2 = new Category(29);
        Set<Category> categories = new HashSet<>();
        categories.add(category_1);
        categories.add(category_2);

        brand.setCategories(categories);

        Brand savedBrand = repo.save(brand);
        System.out.println(savedBrand);

        assertThat(savedBrand.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindAllBrands() {
        Iterable<Brand> brands = repo.findAll();

        for (Brand brand : brands) {
            System.out.println(brand);
        }
    }

    @Test
    public void testFindBrandById() {
        Brand brand = repo.findById(1).get();

        System.out.println(brand);
    }

    @Test
    public void testUpdateBrand() {
        Brand brand = repo.findById(4).get();
        brand.setName("Samsung Electronics");

        Brand savedBrand = repo.save(brand);
        assertThat(savedBrand.getName()).isEqualTo("Samsung Electronics");
    }
}
