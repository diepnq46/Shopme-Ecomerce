package com.shopme.service.impl;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.CountryRepository;
import com.shopme.repository.CustomerRepository;
import com.shopme.service.CustomerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {
    private final CountryRepository countryRepository;

    private final PasswordEncoder passwordEncoder;

    private final CustomerRepository customerRepository;


    @Override
    public List<Country> getAllCountries() {
        return countryRepository.findByOrderByNameAsc();
    }

    @Override
    public boolean isEmailUnique(String email) {
        Customer customer = customerRepository.findByEmail(email);

        return customer == null;
    }

    @Override
    @Transactional
    public void registerCustomer(Customer customer) {
        encodePassword(customer);
        customer.setEnabled(false);
        customer.setCreatedTime(new Date());

        String randomCode = RandomString.make(64);
        customer.setVerificationCode(randomCode);
        customer.setAuthenticationType(AuthenticationType.DATABASE);
        System.out.println("Verification code is: " + randomCode);

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public boolean verify(String verificationCode) {
        Customer customer = customerRepository.findByVerificationCode(verificationCode);

        if (customer == null || customer.isEnabled()) {
            return false;
        }
        customerRepository.enable(customer.getId());

        return true;
    }

    @Override
    @Transactional
    public void updateAuthentication(Customer customer, AuthenticationType authenticationType) {
        if (!customer.getAuthenticationType().equals(authenticationType)) {
            customerRepository.updateAuthenticationType(customer.getId(), authenticationType);
        }
    }

    @Override
    public Customer getCustomerByEmail(String email) {
        return customerRepository.findByEmail(email);
    }

    @Override
    public Customer getCustomerByResetPasswordToken(String token) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token không hợp lệ"));

        return customer;
    }

    @Override
    @Transactional
    public void addNewCustomerUponOauthLogin(String email, String name, String code, AuthenticationType authenticationType) {
        Customer customer = new Customer();
        customer.setEmail(email);
        customer.setPassword("");
        setName(customer, name);
        customer.setAddressLine1("");
        customer.setCity("");
        customer.setState("");
        customer.setEnabled(true);
        customer.setCreatedTime(new Date());
        customer.setPhoneNumber("");
        customer.setCountry(countryRepository.findByCode(code));
        customer.setAuthenticationType(authenticationType);
        customer.setPostalCode("");

        customerRepository.save(customer);
    }

    @Override
    @Transactional
    public Customer updateCustomer(Customer customer) {
        Customer customerInDb = customerRepository.findById(customer.getId()).get();

        if (customer.getAuthenticationType().equals(AuthenticationType.DATABASE)) {
            if (customer.getPassword().isEmpty()) {
                customer.setPassword(customerInDb.getPassword());
            } else {
                encodePassword(customer);
            }
        } else {
            customer.setPassword(customerInDb.getPassword());
        }

        if (customer.getAddressLine2().isEmpty()) {
            customer.setAddressLine2(null);
        }
        customer.setVerificationCode(customer.getVerificationCode());
        customer.setCreatedTime(customerInDb.getCreatedTime());
        customer.setEnabled(customerInDb.isEnabled());

        return customerRepository.save(customer);
    }

    @Override
    @Transactional
    public String updateResetPasswordToken(String email) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByEmail(email);
        String token = null;
        if (customer != null) {
            token = RandomString.make(30);
            customer.setResetPasswordToken(token);

            customerRepository.save(customer);
        } else {
            throw new ResourceNotFoundException("Không tìm thấy khách hàng!");
        }

        return token;
    }

    @Override
    @Transactional
    public void updatePassword(String token, String password) throws ResourceNotFoundException {
        Customer customer = customerRepository.findByResetPasswordToken(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token không hợp lệ"));

        customer.setPassword(password);
        encodePassword(customer);

        customerRepository.save(customer);
    }

    private void setName(Customer customer, String name) {
        String[] nameArray = name.split(" ");
        if (nameArray.length < 2) {
            customer.setFirstName(name);
            customer.setLastName("");
        } else {
            String firstName = nameArray[nameArray.length - 1];
            String lastName = name.replace(firstName, "");

            customer.setFirstName(firstName);
            customer.setLastName(lastName);
        }

    }

    private void encodePassword(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    }
}
