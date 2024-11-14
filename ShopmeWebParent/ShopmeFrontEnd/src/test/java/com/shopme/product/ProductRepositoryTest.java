package com.shopme.product;

import com.shopme.common.entity.Category;
import com.shopme.common.entity.Product;
import com.shopme.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository repo;

    @Test
    public void testFindByAlias() {
        String alias = "sony-zv-1";

        Product product = repo.findByAlias(alias).get();

        assertThat(product).isNotNull();
    }
}
