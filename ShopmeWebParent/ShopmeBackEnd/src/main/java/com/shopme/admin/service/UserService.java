package com.shopme.admin.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    List<User> getAllUsers();

    List<Role> getAllRoles();

    User save(User user);

    boolean isEmailUnique(Integer userId, String email);

    User findUserByEmail(String email);

    User updateAccount(User userInform);

    User findUserById(Integer userId) throws ResourceNotFoundException;

    void deleteUserById(Integer userId);

    void updateEnabledStatus(Integer userId, boolean enabled);

    Page<User> getUserByPage(Integer pageNumber, PagingAndSortingHelper helper);
}
