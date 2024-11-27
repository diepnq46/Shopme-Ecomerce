package com.shopme.service.impl;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;
import com.shopme.repository.ShippingRateRepository;
import com.shopme.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateServiceImpl implements ShippingRateService {
    @Autowired
    private ShippingRateRepository repo;

    @Override
    public ShippingRate getShippingRateForAddress(Address address) {
        String state = address.getState();
        if (state == null || state.isEmpty()) {
            state = address.getCity();
        }

        return repo.findByCountryAndState(address.getCountry(), state);
    }

    @Override
    public ShippingRate getShippingRateForCustomer(Customer customer) {
        String state = customer.getState();
        if (state == null || state.isEmpty()) {
            state = customer.getCity();
        }

        return repo.findByCountryAndState(customer.getCountry(), state);
    }
}
