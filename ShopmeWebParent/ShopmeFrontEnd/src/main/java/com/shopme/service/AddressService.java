package com.shopme.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;

import java.util.List;

public interface AddressService {
    List<Address> listAddressBook(Customer customer);

    void deleteAddress(Integer addressId, Customer customer);

    Address getAddress(Integer addressId, Customer customer) throws ResourceNotFoundException;

    Address saveAddress(Address address);

    void setDefaultAddress(Integer addressId, Customer customer);

    Address getDefaultAddress(Customer customer);
}
