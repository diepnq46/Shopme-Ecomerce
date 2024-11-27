package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Table(name = "customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(length = 45, nullable = false, unique = true)
    private String email;

    @Column(length = 64, nullable = false)
    private String password;

    @Column(length = 45, nullable = false)
    private String firstName;

    @Column(length = 45, nullable = false)
    private String lastName;

    @Column(length = 15, nullable = false)
    private String phoneNumber;

    @Column(length = 64, nullable = false)
    private String addressLine1;

    @Column(length = 64)
    private String addressLine2;

    @Column(length = 45, nullable = false)
    private String city;

    @Column(length = 45)
    private String state;

    @ManyToOne
    @JoinColumn(name = "country_id", nullable = false)
    private Country country;

    @Column(length = 10, nullable = false)
    private String postalCode;

    @Column(nullable = false)
    private Date createdTime;

    private boolean enabled;

    @Column(length = 64)
    private String verificationCode;

    @Enumerated(EnumType.STRING)
    @Column(name = "authentication_type", length = 10)
    private AuthenticationType authenticationType;

    @Column(name = "reset_password_token", length = 30)
    private String resetPasswordToken;

    public Customer(Integer id) {
        this.id = id;
    }

    @Transient
    public String getFullName() {
        return lastName + " " + firstName;
    }

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
}
