package com.shopme.admin.repository;

import com.shopme.common.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    Customer findByEmail(String email);

    Customer findByPostalCode(String postalCode);

    @Query("SELECT c FROM Customer c WHERE" +
            " CONCAT(c.id, ' ', c.firstName, ' ', c.lastName , ' ', c.email, ' ', c.city, ' ', c.state, ' ', c.country.name) " +
            " LIKE %?1%"
    )
    Page<Customer> search(String keyword, Pageable pageable);

    @Query("UPDATE Customer c SET c.enabled = true, c.verificationCode = null WHERE c.id = ?1")
    @Modifying
    void enable(Integer id);


    boolean existsByEmail(String email);

    @Query("UPDATE Customer c SET c.enabled = ?2 WHERE c.id = ?1")
    @Modifying
    void updateEnabledStatus(Integer id, Boolean enabled);
}
