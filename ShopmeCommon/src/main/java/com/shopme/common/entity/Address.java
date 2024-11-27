package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private  Country country;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @Column(name = "first_name", length = 45)
    private String firstName;

    @Column(name = "last_name", length = 45)
    private String lastName;

    @Column(name = "phone_number", length = 15, nullable = false)
    private String phoneNumber;

    @Column(name = "address_line1", length = 64, nullable = false)
    private String addressLine1;

    @Column(name = "address_line2", length = 64)
    private String addressLine2;

    @Column(length = 45)
    private String city;

    @Column(length = 45)
    private String state;

    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @Column(name = "default_address", nullable = false)
    private boolean defaultAddress;

    @Transient
    public String getAddress() {
        String address = firstName;

        if (lastName != null && !lastName.isEmpty()) {address = lastName + " " + address;}

        if (!addressLine1.isEmpty()) {address += ", " + addressLine1;}
        if (addressLine2 != null && !addressLine2.isEmpty()) {address += ", " + addressLine2;}

        if (city != null && !city.isEmpty()) {address += ", " + city;}
        if (!state.isEmpty()) {address += ", " + state;}
        address += ", " + country.getName();

        if(!postalCode.isEmpty()) {address += ". Mã bưu chính: " + postalCode;}
        if(!phoneNumber.isEmpty()) {address += ". Điện thoại: " + phoneNumber;}

        return address;
    }

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", country=" + country.getName() +
                ", customer=" + customer +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", city='" + city + '\'' +
                ", state='" + state + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", defaultAddress=" + defaultAddress +
                '}';
    }
}
