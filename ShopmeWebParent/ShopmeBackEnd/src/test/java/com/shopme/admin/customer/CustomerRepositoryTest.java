package com.shopme.admin.customer;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.annotation.Rollback;

import java.util.Date;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class CustomerRepositoryTest {
    @Autowired
    private CustomerRepository repo;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void testCreateCustomer1() {
        int countryId = 234; //US
        Country country = entityManager.find(Country.class, countryId);

        Customer customer = new Customer();
        customer.setCountry(country);
        customer.setEmail("davidgnob@gmail.com");
        customer.setPassword("password123");
        customer.setFirstName("David");
        customer.setLastName("Gnob");
        customer.setState("New York");
        customer.setCity("New York");
        customer.setAddressLine1("1st Street");
        customer.setPhoneNumber("0123456789");
        customer.setCreatedTime(new Date());
        customer.setPostalCode("98567");

        repo.save(customer);
    }
}
