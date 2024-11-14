package com.shopme.admin.service.impl;

import com.shopme.common.dto.StateDto;
import com.shopme.admin.repository.StateRepository;
import com.shopme.admin.service.StateService;
import com.shopme.common.entity.Country;
import com.shopme.common.entity.State;
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

    @Override
    public StateDto saveState(State state) {
        State savedState = repo.save(state);

        return new StateDto(savedState);
    }

    @Override
    public void deleteState(Integer id) {
        repo.deleteById(id);
    }
}
