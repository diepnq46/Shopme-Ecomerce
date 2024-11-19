package com.shopme.admin.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface ShippingRateService {
    Page<ShippingRate> listByPage(int pageNum, PagingAndSortingHelper helper);

    ShippingRate saveShippingRate(ShippingRate shippingRate);

    boolean isUniqueShippingRate(Integer id, Integer countryId, String state);

    void updateSupportedCOD(Integer id, boolean supported) throws ResourceNotFoundException;

    ShippingRate getShippingRateById(Integer id) throws ResourceNotFoundException;

    void deleteShippingRateById(Integer id);
}
