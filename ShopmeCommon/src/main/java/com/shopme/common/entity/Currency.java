package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencys")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Currency extends IdBasedEntity{
    @Column(length = 64, nullable = false)
    private String name;

    @Column(length = 3, nullable = false)
    private String symbol;

    @Column(length = 4, nullable = false)
    private String code;

    @Override
    public String toString() {
        return name + " - " + symbol + " - " + code;
    }
}
