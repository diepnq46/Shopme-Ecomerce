package com.shopme.admin.controller;

import com.shopme.admin.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserRestController {
    private final UserService service;

    @PostMapping("/check-email")
    public String checkDuplicateEmail(@Param("id") Integer id,
                                      @Param("email") String email) {
        return service.isEmailUnique(id, email) ? "OK" : "DUPLICATED";
    }
}
