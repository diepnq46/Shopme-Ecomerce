package com.shopme.service.impl;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
import com.shopme.common.dto.StateDto;
import com.shopme.repository.StateRepository;
import com.shopme.service.StateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StateServiceImpl implements StateService {
    @Autowired
    private StateRepository repo;

    @Override
    public List<StateDto> getStatesByCountry(Integer countryId) {
        List<State> states = repo.findByCountryOrderByNameAsc(new Country(countryId));

        return states.stream().map(StateDto::new).toList();
    }
}
