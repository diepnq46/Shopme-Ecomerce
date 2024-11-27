package com.shopme.shipping_rate;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.repository.ShippingRateRepository;
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
    public void testFindByCountryAndState() {
        Country country = new Country(234);
        String state = "Florida";

        ShippingRate shippingRate = repo.findByCountryAndState(country, state);

        assertThat(shippingRate).isNotNull();
    }
}
