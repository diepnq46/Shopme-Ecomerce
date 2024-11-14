package com.shopme.admin.repository;

import com.shopme.common.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    @Nullable
    User findByEmail(String email);

    @Query("SELECT u FROM User u WHERE " +
            "CONCAT(u.id, ' ', u.email, ' ', u.lastName, ' ', u.firstName) " +
            "LIKE %?1%")
    Page<User> findAll(String keyword, Pageable pageable);

    @Query("UPDATE User u SET u.enabled = :enabled WHERE u.id = :userId")
    @Modifying
    void updateEnabledStatus(@Param("userId") Integer id, @Param("enabled") boolean enabled);
}
