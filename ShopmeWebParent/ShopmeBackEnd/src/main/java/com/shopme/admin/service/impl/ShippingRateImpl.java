package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.repository.ShippingRateRepository;
import com.shopme.admin.service.ShippingRateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class ShippingRateImpl implements ShippingRateService {
    public static int SHIPPING_RATE_PER_PATE = 10;

    @Autowired
    private ShippingRateRepository shippingRateRepo;

    @Autowired
    private CountryRepository countryRepo;

    @Override
    public Page<ShippingRate> listByPage(int pageNum, PagingAndSortingHelper helper) {
        String sortDir = helper.getSortDir();
        String sortField = helper.getSortField();
        String keyword = helper.getKeyword();

        Sort sort = sortField.equals("country") ? Sort.by("country.name") : Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        Pageable pageable = PageRequest.of(pageNum - 1, SHIPPING_RATE_PER_PATE, sort);

        if (keyword != null && !keyword.isEmpty()) {
            return shippingRateRepo.search(keyword, pageable);
        }

        return shippingRateRepo.findAll(pageable);
    }

    @Override
    @Transactional
    public ShippingRate saveShippingRate(ShippingRate shippingRate) {
        return shippingRateRepo.save(shippingRate);
    }

    @Override
    public boolean isUniqueShippingRate(Integer id, Integer countryId, String state) {
        boolean isUpdating = id != null;
        Country country = countryRepo.findById(countryId).get();

        ShippingRate shippingRate = shippingRateRepo.findByCountryAndState(country, state);
        if (shippingRate != null) {
            if (isUpdating) {
                return shippingRate.getId().equals(id);
            }

            return false;
        }

        return true;
    }

    @Override
    @Transactional
    public void updateSupportedCOD(Integer id, boolean supported) throws ResourceNotFoundException {
        shippingRateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảng giá có ID: " + id));

        shippingRateRepo.updateSupportedCOD(id, supported);
    }

    @Override
    public ShippingRate getShippingRateById(Integer id) throws ResourceNotFoundException {
        return shippingRateRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy bảng giá có ID: " + id));
    }

    @Override
    @Transactional
    public void deleteShippingRateById(Integer id) {
        shippingRateRepo.deleteById(id);
    }
}
