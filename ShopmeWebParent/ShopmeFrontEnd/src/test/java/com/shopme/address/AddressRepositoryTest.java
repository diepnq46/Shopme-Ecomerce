package com.shopme.address;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.repository.AddressRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(false)
public class AddressRepositoryTest {
    @Autowired
    private AddressRepository repo;

    @Test
    public void testCreateAddress() {
        Address address = new Address();
        Customer customer = new Customer(1);
        Country country = new Country(242);

        address.setCountry(country);
        address.setCustomer(customer);
        address.setState("Nam Dinh");
        address.setAddressLine1("đường Kim Đồng");
        address.setAddressLine2("huyện Nghĩa Hưng");
        address.setCity("Nam Dinh");
        address.setFirstName("Diep");
        address.setLastName("Ngo");
        address.setPostalCode("1234");
        address.setPhoneNumber("0111222333");
        address.setDefaultAddress(false);
        Address savedAddress = repo.save(address);

        assertThat(savedAddress.getId()).isGreaterThan(0);
    }

    @Test
    public void testFindDefaultAddressByCustomer() {
        int customerId = 5;
        Address address = repo.findDefaultByCustomer(customerId);

        System.out.println(address);

        assertThat(address).isNotNull();
    }
}
