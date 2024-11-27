package com.shopme.repository;

import com.shopme.common.entity.Address;
import com.shopme.common.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Integer> {
    List<Address> findByCustomer(Customer customer);

    @Query("SELECT a FROM Address a WHERE a.id = ?1 AND a.customer.id = ?2")
    Optional<Address> findByIdAndCustomer(Integer addressId, Integer customerId);

    @Modifying
    @Query("DELETE FROM Address a WHERE  a.id = ?1 AND a.customer.id = ?2")
    void deleteByIdAndCustomer(Integer addressId, Integer customerId);

    @Modifying
    @Query("UPDATE Address a SET a.defaultAddress = true WHERE a.id = ?1")
    void setDefaultAddress(Integer addressId);

    @Modifying
    @Query("UPDATE Address a SET a.defaultAddress = false WHERE a.customer.id = ?2 AND a.id != ?1")
    void setNonDefaultAddress(Integer defaultAddressId, Integer customerId);

    @Query("SELECT a FROM Address a WHERE a.customer.id = ?1 AND a.defaultAddress = true")
    Address findDefaultByCustomer(Integer customerId);
}
