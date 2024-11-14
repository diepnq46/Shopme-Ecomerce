package com.shopme.admin.service;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface CustomerService {
    Page<Customer> listByPage(Integer pageNum, String sortField, String sortDir, String keyword);

    Customer updateCustomer(Customer customer) throws ResourceNotFoundException;

    void updateEnabledStatus(Integer customerId, boolean enabled);

    void deleteCustomer(Integer customerId);

    Customer getCustomer(Integer customerId) throws ResourceNotFoundException;

    boolean existsCustomerByEmail(Integer id, String email);
}
