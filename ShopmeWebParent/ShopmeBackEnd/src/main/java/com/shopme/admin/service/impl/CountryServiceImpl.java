package com.shopme.admin.service.impl;

import com.shopme.admin.repository.CountryRepository;
import com.shopme.admin.service.CountryService;
import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryServiceImpl implements CountryService {
    @Autowired
    private CountryRepository repo;

    @Override
    public List<Country> getAllCountries() {
        return repo.findByOrderByNameAsc();
    }

    @Override
    public Country saveCountry(Country country) {
        return repo.save(country);
    }

    @Override
    public void deleteCountry(Integer id) {
        repo.deleteById(id);
    }
}
