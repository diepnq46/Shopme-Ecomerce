package com.shopme.admin.product;

import com.shopme.admin.repository.ProductRepository;
import com.shopme.common.entity.Brand;
import com.shopme.common.entity.Category;
import com.shopme.common.entity.product.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateProduct() {
        Brand brand = entityManager.find(Brand.class, 37);
        Category category = entityManager.find(Category.class, 5);

        Product product = new Product();
        product.setName("Acer Aspire Desktop");
        product.setAlias("acer-aspire-desktop");
        product.setShortDescription("Short description for Acer Aspire Desktop");
        product.setFullDescription("Full description for Acer Aspire Desktop");

        product.setCreatedTime(new Date());
        product.setUpdatedTime(new Date());

        product.setEnabled(true);
        product.setInStock(true);

        product.setPrice(678);
        product.setCost(600);

        product.setBrand(brand);
        product.setCategory(category);

        Product savedProduct = repo.save(product);

        Assertions.assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void listAllProducts() {
        Iterable<Product> products = repo.findAll();

        products.forEach(System.out::println);
    }

    @Test
    public void testGetProduct() {
        int id = 2;
        Product product = repo.findById(id).get();
        System.out.println(product);

        Assertions.assertThat(product.getId()).isEqualTo(id);
    }

    @Test
    public void testUpdateProduct() {
        int id = 1;
        Product product = repo.findById(id).get();
        product.setPrice(499);

        Product savedProduct = repo.save(product);

        Assertions.assertThat(savedProduct.getPrice()).isEqualTo(499);
    }

    @Test
    public void testDeleteProduct() {
        int id = 1;
        repo.deleteById(id);

        Optional<Product> product = repo.findById(id);

        Assertions.assertThat(!product.isPresent());
    }

    @Test
    public void testFindProductByName() {
        String name = "Dell Inspiron 3000";

        Product product = repo.findByName(name);

        Assertions.assertThat(product.getId()).isGreaterThan(0);
    }

    @Test
    public void testSaveProductWithImages() {
        int id = 2;
        Product product = repo.findById(id).get();

        product.setMainImage("main image.jpg");
        product.addExtraImage("extra image 1.jpg");
        product.addExtraImage("extra_image_2.jpg");
        product.addExtraImage("extra-image-3.jpg");

        Product savedProduct = repo.save(product);

        Assertions.assertThat(savedProduct.getImages().size()).isEqualTo(3);
    }

    @Test
    public void testSaveProductWithDetails() {
        int id = 3;
        Product product = repo.findById(id).get();

        product.addDetail("Bộ nhớ thiết bị", "512GB");
        product.addDetail("CPU Model", "MediaTek");
        product.addDetail("OS", "Windows 10");

        Product savedProduct = repo.save(product);
        Assertions.assertThat(savedProduct.getDetails().size()).isEqualTo(3);
    }
}
