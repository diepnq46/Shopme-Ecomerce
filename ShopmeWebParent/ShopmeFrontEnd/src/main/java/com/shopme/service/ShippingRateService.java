package com.shopme.service;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.ShippingRate;

public interface ShippingRateService {
    ShippingRate getShippingRateForAddress(Address address);

    ShippingRate getShippingRateForCustomer(Customer customer);
}
