package com.shopme.customer;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.repository.CustomerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repo;

    @Test
    public void testUpdateAuthenticationType() {
        int customerId = 1;

        repo.updateAuthenticationType(customerId, AuthenticationType.GOOGLE);
    }
}
