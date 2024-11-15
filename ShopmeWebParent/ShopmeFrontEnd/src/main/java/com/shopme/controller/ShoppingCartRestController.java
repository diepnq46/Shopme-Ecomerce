package com.shopme.controller;

import com.shopme.common.entity.Customer;
import com.shopme.common.exception.ResourceNotFoundException;
import com.shopme.service.CustomerService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/carts")
public class ShoppingCartRestController {
    @Autowired
    private CustomerService customerService;

    @Autowired
    private ShoppingCartService cartService;

    @GetMapping("/add/{productId}/{quantity}")
    public String addToCart(@PathVariable("productId") Integer productId,
                            @PathVariable("quantity") Integer quantity,
                            HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            Integer updatedQuantity = cartService.addToCart(quantity, productId, customer);

            return updatedQuantity + " mặt hàng của sản phẩm này đã được thêm vào giỏ hàng của bạn";
        } catch (ResourceNotFoundException e) {
            return "Bạn cần đăng nhập để thêm sản phẩm vào giỏ hàng";
        }
    }

    @GetMapping("/update/{productId}/{quantity}")
    public String updateQuantity(@PathVariable("productId") Integer productId,
                                 @PathVariable("quantity") Integer quantity, HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            float subTotal = cartService.updateQuantity(quantity, productId, customer);

            return String.valueOf(subTotal);
        } catch (ResourceNotFoundException e) {
            return "Bạn cần đăng nhập để thực hện hành động này";
        }
    }

    @DeleteMapping("/{productId}")
    public String deleteCartItem(@PathVariable("productId") Integer productId, HttpServletRequest request) {
        try {
            Customer customer = getAuthenticatedCustomer(request);
            cartService.deleteCartItem(productId, customer);

            return "Sản phẩm đã được xóa khỏi giỏ hàng";
        } catch (ResourceNotFoundException e) {
            return "Bạn cần đăng nhập để thực hện hành động này";
        }
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) throws ResourceNotFoundException {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);
        Customer customer = customerService.getCustomerByEmail(email);

        if (customer == null) {
            throw new ResourceNotFoundException("No authenticated customer");
        }

        return customer;
    }
}
