package com.shopme.service;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.ShippingRate;
import com.shopme.entity.CheckoutInfo;

import java.util.List;

public interface CheckoutService {
    CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate);
}
