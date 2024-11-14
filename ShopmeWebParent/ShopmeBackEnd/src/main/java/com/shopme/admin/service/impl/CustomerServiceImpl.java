package com.shopme.admin.service.impl;

import com.shopme.admin.repository.CustomerRepository;
import com.shopme.admin.service.CustomerService;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class CustomerServiceImpl implements CustomerService {
    public static int CUSTOMERS_PER_PAGE = 10;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    @Override
    public Page<Customer> listByPage(Integer pageNum, String sortField, String sortDir, String keyword) {
        Sort sort = sortField.equals("country") ? Sort.by("country.name") : Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, CUSTOMERS_PER_PAGE, sort);
        if (keyword != null) {
            return customerRepository.search(keyword, pageable);
        }

        return customerRepository.findAll(pageable);
    }


    @Override
    @Transactional
    public Customer updateCustomer(Customer customer) throws ResourceNotFoundException {
        Customer customerInDb = customerRepository.findById(customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + customer.getId()));

        if (customer.getPassword().isEmpty()) {
            customer.setPassword(customerInDb.getPassword());
        } else{
            encodePassword(customer);
        }

        if (customer.getAddressLine2().isEmpty()) {
            customer.setAddressLine2(null);
        }
        customer.setVerificationCode(customer.getVerificationCode());
        customer.setCreatedTime(customerInDb.getCreatedTime());
        customer.setEnabled(customerInDb.isEnabled());

        return customerRepository.save(customer);
    }

    private void encodePassword(Customer customer) {
        customer.setPassword(passwordEncoder.encode(customer.getPassword()));
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Integer customerId, boolean enabled) {
        customerRepository.updateEnabledStatus(customerId, enabled);
    }

    @Override
    public void deleteCustomer(Integer customerId) {
        customerRepository.deleteById(customerId);
    }

    @Override
    public Customer getCustomer(Integer customerId) throws ResourceNotFoundException {
        return customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy khách hàng với ID: " + customerId));
    }

    @Override
    public boolean existsCustomerByEmail(Integer id, String email) {
        Customer customer = customerRepository.findByEmail(email);

        if (customer == null) {
            return false;
        }

        return !customer.getId().equals(id);
    }
}
