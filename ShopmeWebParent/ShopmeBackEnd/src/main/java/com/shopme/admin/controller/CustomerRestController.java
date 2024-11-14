package com.shopme.admin.controller;

import com.shopme.admin.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
    @Autowired
    private CustomerService service;

    @PostMapping("/check-email")
    public String checkUniqueEmail(@RequestParam("id") Integer id,
                                   @RequestParam("email") String email) {

        return service.existsCustomerByEmail(id, email) ? "DUPLICATED" : "OK";
    }
}
