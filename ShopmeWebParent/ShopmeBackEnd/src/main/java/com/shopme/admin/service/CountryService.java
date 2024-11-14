package com.shopme.admin.service;

import com.shopme.common.entity.Country;

import java.util.List;

public interface CountryService {
    List<Country> getAllCountries();

    Country saveCountry(Country country);

    void deleteCountry(Integer id);
}
