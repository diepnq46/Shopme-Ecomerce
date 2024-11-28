package com.shopme.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public class AbstractAddress extends IdBasedEntity{
    @Column(name = "first_name", length = 45)
    protected String firstName;

    @Column(name = "last_name", length = 45)
    protected String lastName;

    @Column(name = "phone_number", length = 15, nullable = false)
    protected String phoneNumber;

    @Column(name = "address_line_1", length = 64, nullable = false)
    protected String addressLine1;

    @Column(name = "address_line_2", length = 64)
    protected String addressLine2;

    @Column(length = 45)
    protected String city;

    @Column(length = 45)
    protected String state;

    @Column(name = "postal_code", length = 10, nullable = false)
    protected String postalCode;
}
