package com.shopme.controller;

import com.shopme.common.dto.StateDto;
import com.shopme.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateRestController {
    @Autowired
    private StateService service;

    @GetMapping("/list-by-country/{countryId}")
    public List<StateDto> listStatesByCountry(@PathVariable int countryId) {
        return service.getStatesByCountry(countryId);
    }
}
