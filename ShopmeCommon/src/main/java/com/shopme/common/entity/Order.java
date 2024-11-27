package com.shopme.common.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "orders")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

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

    @Column(length = 45, nullable = false)
    private String state;

    @Column(name = "postal_code", length = 10, nullable = false)
    private String postalCode;

    @Column(length = 45, nullable = false)
    private String country;

    private Date orderTime;

    @Column(name = "deliver_days")
    private int deliverDays;

    @Column(name = "deliver_date")
    private Date deliverDate;

    private float shippingCost;
    private float productCost;
    private float subTotal;
    private float tax;
    private float total;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private Set<OrderDetail> orderDetails = new HashSet<>();

    @Transient
    public String getFullName() {
        return lastName + " " + firstName;
    }


    @Transient
    public String getDestination() {
        String address = "";
        if (!addressLine1.isEmpty()) {address += addressLine1;}
        if (addressLine2 != null && !addressLine2.isEmpty()) {address += ", " + addressLine2;}

        if (city != null && !city.isEmpty()) {address += ", " + city;}
        if (!state.isEmpty()) {address += ", " + state;}
        address += ", " + country;

        return address;
    }
}
