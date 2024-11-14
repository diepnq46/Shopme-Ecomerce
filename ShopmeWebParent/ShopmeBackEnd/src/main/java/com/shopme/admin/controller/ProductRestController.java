package com.shopme.admin.controller;

import com.shopme.admin.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/products")
public class ProductRestController {
    @Autowired
    private ProductService service;

    @PostMapping("/check-unique")
    public String checkUniqueProduct(@Param("id") Integer id,
                                     @Param("name") String name) {
        return service.isUniqueProduct(id, name) ? "OK" : "Duplicated";
    }
}
