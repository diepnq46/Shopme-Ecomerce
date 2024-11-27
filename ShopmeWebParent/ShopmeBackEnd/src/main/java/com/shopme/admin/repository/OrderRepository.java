package com.shopme.admin.repository;

import com.shopme.common.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    @Query("SELECT o FROM Order o WHERE " +
            "CONCAT(o.lastName, ' ', o.firstName) LIKE %?1% " +
            "OR o.phoneNumber LIKE %?1% " +
            "OR o.addressLine1 LIKE %?1% OR o.addressLine2 LIKE %?1% " +
            "OR CONCAT(o.customer.lastName, ' ',o.customer.firstName) LIKE %?1% " +
            "OR o.postalCode LIKE %?1% OR o.city LIKE %?1% " +
            "OR o.country LIKE %?1% OR o.state LIKE %?1% " +
            "OR o.paymentMethod LIKE %?1% " +
            "OR o.orderStatus LIKE %?1%")
    Page<Order> search(String keyword, Pageable pageable);
}
