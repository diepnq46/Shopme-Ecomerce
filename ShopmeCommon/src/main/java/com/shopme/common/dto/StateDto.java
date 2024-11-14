package com.shopme.common.dto;
import com.shopme.common.entity.State;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StateDto {
    private Integer id;
    private String name;

    public StateDto(State state) {
        if (state != null) {
            this.id = state.getId();
            this.name = state.getName();
        }
    }
}
