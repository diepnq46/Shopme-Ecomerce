package com.shopme.admin.service;

import com.shopme.common.dto.StateDto;
import com.shopme.common.entity.State;

import java.util.List;

public interface StateService {
    List<StateDto> getStatesByCountry(Integer countryId);

    StateDto saveState(State state);

    void deleteState(Integer id);
}
