package com.shopme.service.impl;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.repository.AddressRepository;
import com.shopme.service.AddressService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepo;


    @Override
    public List<Address> listAddressBook(Customer customer) {
        return addressRepo.findByCustomer(customer);
    }

    @Override
    @Transactional
    public void deleteAddress(Integer addressId, Customer customer) {
        addressRepo.deleteByIdAndCustomer(addressId, customer.getId());
    }

    @Override
    public Address getAddress(Integer addressId, Customer customer) throws ResourceNotFoundException {
        return addressRepo.findByIdAndCustomer(addressId, customer.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy địa chỉ với ID: " + addressId));
    }

    @Override
    public Address saveAddress(Address address) {
        return addressRepo.save(address);
    }

    @Override
    @Transactional
    public void setDefaultAddress(Integer addressId, Customer customer) {
        if (addressId > 0) {
            addressRepo.setDefaultAddress(addressId);
        }

        addressRepo.setNonDefaultAddress(addressId, customer.getId());
    }

    @Override
    public Address getDefaultAddress(Customer customer) {
        return addressRepo.findDefaultByCustomer(customer.getId());
    }
}
