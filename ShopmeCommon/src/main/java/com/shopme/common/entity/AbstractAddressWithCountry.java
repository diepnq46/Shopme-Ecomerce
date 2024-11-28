package com.shopme.common.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@MappedSuperclass
@Getter
@Setter
public abstract class AbstractAddressWithCountry extends AbstractAddress {
    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    protected Country country;

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) {
            address = lastName + " " + address;
        }

        if (!addressLine1.isEmpty()) {
            address += ", " + addressLine1;
        }
        if (addressLine2 != null && !addressLine2.isEmpty()) {
            address += ", " + addressLine2;
        }

        if (city != null && !city.isEmpty()) {
            address += ", " + city;
        }
        if (!state.isEmpty()) {
            address += ", " + state;
        }
        address += ", " + country.getName();

        if (postalCode != null && !postalCode.isEmpty()) {
            address += ". Mã bưu chính: " + postalCode;
        }
        if (!phoneNumber.isEmpty()) {
            address += ". Điện thoại: " + phoneNumber;
        }

        return address;
    }

}
