package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "shipping_rates")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingRate extends IdBasedEntity{
    @Column(nullable = false)
    private float rate;

    @Column(nullable = false)
    private int days;

    @Column(name = "cod_supported", nullable = false)
    private boolean codSupported;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(length = 45, nullable = false)
    private String state;
}
