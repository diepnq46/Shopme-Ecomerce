package com.shopme.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;

import java.util.List;

public interface ShoppingCartService {
    Integer addToCart(Integer quantity, Integer productId, Customer customer);

    List<CartItem> getByCustomer(Customer customer);
}
