package com.shopme.controller;

import com.shopme.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
    @Autowired
    private CustomerService service;

    @PostMapping("/check-unique-email")
    public String checkUniqueEmail(@RequestParam("email") String email) {
        return service.isEmailUnique(email) ? "OK" : "DUPLICATED";
    }
}
