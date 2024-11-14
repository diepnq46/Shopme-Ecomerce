package com.shopme.service;

import com.shopme.common.dto.StateDto;

import java.util.List;

public interface StateService {
    List<StateDto> getStatesByCountry(Integer countryId);
}
