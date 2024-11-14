package com.shopme.admin.controller;

import com.shopme.admin.service.CountryService;
import com.shopme.common.entity.Country;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryRestController {
    @Autowired
    private CountryService service;

    @GetMapping
    public List<Country> getAllCountries() {
        List<Country> countries = service.getAllCountries();
        return countries;
    }

    @PostMapping
    public Integer saveCountry(@RequestBody Country country) {
        Country savedCountry = service.saveCountry(country);

        return savedCountry.getId();
    }


    @DeleteMapping("/{id}")
    public void deleteCountry(@PathVariable Integer id) {
        service.deleteCountry(id);
    }
}
