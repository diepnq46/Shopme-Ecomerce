package com.shopme.service;

import com.shopme.common.entity.AuthenticationType;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;

import java.util.List;

public interface CustomerService {
    List<Country> getAllCountries();

    boolean isEmailUnique(String email);

    void registerCustomer(Customer customer);

    boolean verify(String verificationCode);

    void updateAuthentication(Customer customer, AuthenticationType authenticationType);

    Customer getCustomerByEmail(String email);

    Customer getCustomerByResetPasswordToken(String token) throws ResourceNotFoundException;

    void addNewCustomerUponOauthLogin(String email, String name, String code, AuthenticationType authenticationType);

    Customer updateCustomer(Customer customer);

    String updateResetPasswordToken(String email) throws ResourceNotFoundException;

    void updatePassword(String token, String password) throws ResourceNotFoundException;
}
