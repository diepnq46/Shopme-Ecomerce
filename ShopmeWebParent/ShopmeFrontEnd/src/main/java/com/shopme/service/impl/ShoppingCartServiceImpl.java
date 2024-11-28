package com.shopme.service.impl;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.Customer;
import com.shopme.common.entity.product.Product;
import com.shopme.repository.CartItemRepository;
import com.shopme.repository.ProductRepository;
import com.shopme.service.ShoppingCartService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    @Autowired
    private CartItemRepository cartItemRepo;

    @Autowired
    private ProductRepository productRepo;

    @Override
    @Transactional
    public Integer addToCart(Integer quantity, Integer productId, Customer customer) {
        Product product = new Product(productId);
        CartItem cartItem = cartItemRepo.findByCustomerAndProduct(customer, product);
        Integer updatedQuantity = quantity;

        if (cartItem == null) {
            cartItem = new CartItem();
            cartItem.setCustomer(customer);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
        }else {
            updatedQuantity = cartItem.getQuantity() + quantity;

            if (updatedQuantity > 5) {
                cartItem.setQuantity(5);
            }
            cartItem.setQuantity(updatedQuantity);
        }

        cartItemRepo.save(cartItem);
        return updatedQuantity;
    }

    @Override
    public List<CartItem> getByCustomer(Customer customer) {
        return cartItemRepo.findByCustomer(customer);
    }

    @Override
    @Transactional
    public float updateQuantity(Integer quantity, Integer productId, Customer customer) {
        cartItemRepo.updateQuantity(quantity, customer.getId(), productId);
        Product product = productRepo.findById(productId).get();
        return quantity * product.getDiscountPrice();
    }

    @Override
    @Transactional
    public void deleteCartItem(Integer productId, Customer customer) {
        cartItemRepo.deleteByCustomerAndProduct(customer.getId(), productId);
    }
}
