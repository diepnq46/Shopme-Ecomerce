package com.shopme.admin.controller;

import com.shopme.admin.service.ShippingRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/shipping-rates")
public class ShippingRateRestController {
    @Autowired
    private ShippingRateService service;

    @PostMapping("/check-unique")
    public String checkUnique(@Param("id") Integer id,
                              @Param("countryId") Integer countryId,
                              @Param("state") String state) {
        return service.isUniqueShippingRate(id, countryId, state) ? "OK" : "DUPLICATED";
    }
}
