package com.shopme.admin.setting.country;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.common.entity.Country;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CountryRepositoryTest {
    @Autowired
    private CountryRepository repo;

    @Test
    public void testCreateCountries() {
        List<Country> countries = List.of(
                new Country("Republic of India", "IN"),
                new Country("United States", "US"),
                new Country("United Kingdom", "UK"),
                new Country("Vietnam", "VN"),
                new Country("Germany", "GT")
        );

        repo.saveAll(countries);
    }
}
