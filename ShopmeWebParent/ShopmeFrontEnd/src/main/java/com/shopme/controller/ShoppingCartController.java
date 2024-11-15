package com.shopme.controller;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.service.CustomerService;
import com.shopme.service.ShoppingCartService;
import com.shopme.util.Utility;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class ShoppingCartController {
    @Autowired
    private ShoppingCartService cartService;

    @Autowired
    private CustomerService customerService;

    @GetMapping("/cart")
    public String viewCart(Model model, HttpServletRequest request) {
        Customer customer = getAuthenticatedCustomer(request);
        List<CartItem> cartItems = cartService.getByCustomer(customer);

        float estimatedTotal = 0.0f;
        for (CartItem cartItem : cartItems) {
            estimatedTotal += cartItem.getSubTotal();
        }

        model.addAttribute("estimatedTotal", estimatedTotal);
        model.addAttribute("cartItems", cartItems);
        return "cart/shopping_cart";
    }

    private Customer getAuthenticatedCustomer(HttpServletRequest request) {
        String email = Utility.getEmailOfAuthenticatedCustomer(request);

        return customerService.getCustomerByEmail(email);
    }
}
