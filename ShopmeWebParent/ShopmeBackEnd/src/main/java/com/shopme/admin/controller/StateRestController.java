package com.shopme.admin.controller;

import com.shopme.common.entity.State;
import com.shopme.common.dto.StateDto;
import com.shopme.admin.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/states")
public class StateRestController {
    @Autowired
    private StateService service;

    @GetMapping("/list-by-country/{countryId}")
    public List<StateDto> listByCountry(@PathVariable int countryId) {
        return service.getStatesByCountry(countryId);
    }

    @PostMapping
    public StateDto saveState(@RequestBody State state) {
        return service.saveState(state);
    }

    @DeleteMapping("/{stateId}")
    public void deleteState(@PathVariable int stateId) {
        service.deleteState(stateId);
    }
}
