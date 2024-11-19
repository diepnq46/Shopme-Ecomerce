package com.shopme.admin.shippingrates;

import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class ShippingRateRepositoryTest {
    @Autowired
    private ShippingRateRepository repo;

    @Test
    public void testCreateShippingRate() {
        ShippingRate shippingRate = new ShippingRate();
        shippingRate.setRate(4.5f);
        shippingRate.setCodSupported(true);
        shippingRate.setCountry(new Country(242));
        shippingRate.setState("");
        ShippingRate savedShippingRate = repo.save(shippingRate);

        assertThat(savedShippingRate.getId()).isGreaterThan(0);
    }

}
