package com.shopme.admin.repository;

import com.shopme.common.entity.Country;
import com.shopme.common.entity.ShippingRate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShippingRateRepository extends JpaRepository<ShippingRate, Integer> {
    @Query("SELECT s FROM ShippingRate s WHERE " +
            "s.country.name LIKE %?1% " +
            "OR " +
            "s.state LIKE %?1%")
    Page<ShippingRate> search(String keyword, Pageable pageable);

    ShippingRate findByCountryAndState(Country country, String state);

    @Modifying
    @Query("UPDATE ShippingRate sr SET sr.codSupported = ?2 WHERE sr.id = ?1")
    void updateSupportedCOD(Integer id, boolean supported);
}
