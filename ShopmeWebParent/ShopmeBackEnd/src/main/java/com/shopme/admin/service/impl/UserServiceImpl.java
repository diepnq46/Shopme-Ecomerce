package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.RoleRepository;
import com.shopme.admin.repository.UserRepository;
import com.shopme.admin.service.UserService;
import com.shopme.common.entity.Role;
import com.shopme.common.entity.User;
import com.shopme.common.exception.ResourceNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    public static final int USER_PER_PAGE = 8;

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepo.findAll();
    }

    @Override
    @Transactional
    public User save(User user) {
        if (user.getPhotos().isEmpty()) {
            user.setPhotos(null);
        }

        if (user.getId() == null) {
            encodePassword(user);
            return userRepo.save(user);

        }
        User existingUser = userRepo.findById(user.getId()).get();
        if (user.getPassword().isEmpty()) {
            user.setPassword(existingUser.getPassword());
        } else {
            encodePassword(user);
        }

        return userRepo.save(user);
    }

    @Override
    public boolean isEmailUnique(Integer userId, String email) {
        User user = userRepo.findByEmail(email);
        if (user == null) {
            return true;
        }

        boolean isUpdating = (userId != null);
        if (isUpdating) {
            return user.getId().equals(userId);
        }

        return false;
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public User updateAccount(User userInform) {
        User userInDb = userRepo.findById(userInform.getId()).get();

        if (!userInform.getPassword().isEmpty()) {
            encodePassword(userInform);
            userInDb.setPassword(userInform.getPassword());
        }

        if (userInform.getPhotos() != null) {
            userInDb.setPhotos(userInform.getPhotos());
        }

        userInDb.setFirstName(userInform.getFirstName());
        userInDb.setLastName(userInform.getLastName());

        return userRepo.save(userInDb);
    }

    @Override
    public User findUserById(Integer userId) throws ResourceNotFoundException {
        return userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy người dùng có ID: " + userId));
    }

    @Override
    @Transactional
    public void deleteUserById(Integer userId) {
        userRepo.deleteById(userId);
    }

    @Override
    @Transactional
    public void updateEnabledStatus(Integer userId, boolean enabled) {
        userRepo.updateEnabledStatus(userId, enabled);
    }

    @Override
    public Page<User> getUserByPage(Integer pageNumber, PagingAndSortingHelper helper) {
        String sortDir = helper.getSortDir();
        String sortField = helper.getSortField();
        String keyword = helper.getKeyword();

        Sort sort = sortDir.equals("asc") ? Sort.by(sortField).ascending() : Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNumber - 1, USER_PER_PAGE, sort);

        if (keyword == null) {
            return userRepo.findAll(pageable);
        }

        return userRepo.findAll(keyword, pageable);
    }

    private void encodePassword(User user) {
        String encodedPassword = passwordEncoder.encode(user.getPassword());

        user.setPassword(encodedPassword);
    }


}
