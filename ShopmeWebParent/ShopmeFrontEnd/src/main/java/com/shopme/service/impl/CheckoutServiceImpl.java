package com.shopme.service.impl;

import com.shopme.common.entity.CartItem;
import com.shopme.common.entity.product.Product;
import com.shopme.common.entity.ShippingRate;
import com.shopme.entity.CheckoutInfo;
import com.shopme.service.CheckoutService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutServiceImpl implements CheckoutService {
    public static final int DIM_DIVISOR = 139;

    @Override
    public CheckoutInfo prepareCheckout(List<CartItem> cartItems, ShippingRate shippingRate) {
        CheckoutInfo checkoutInfo = new CheckoutInfo();

        float productCost = calculateProductCost(cartItems);
        float productTotal = calculateProductTotal(cartItems);
        float shippingCost = calculateShippingCost(cartItems, shippingRate);

        checkoutInfo.setProductCost(productCost);
        checkoutInfo.setProductTotal(productTotal);
        checkoutInfo.setShippingCostTotal(shippingCost);
        checkoutInfo.setDeliverDays(shippingRate.getDays());
        checkoutInfo.setCodSupported(shippingRate.isCodSupported());

        return checkoutInfo;
    }

    private float calculateShippingCost(List<CartItem> cartItems, ShippingRate shippingRate) {
        float shippingCostTotal = 0.0f;

        for (CartItem cartItem : cartItems) {
            Product product = cartItem.getProduct();

            float dimWeight = (product.getLength() * product.getWidth() * product.getHeight()) / DIM_DIVISOR;
            float finalWeight = Math.max(product.getWeight(), dimWeight);
            float shippingCost = finalWeight * shippingRate.getRate() * cartItem.getQuantity();

            cartItem.setShippingCost(shippingCost);
            shippingCostTotal += shippingCost;
        }

        return shippingCostTotal;
    }

    private float calculateProductTotal(List<CartItem> cartItems) {
        float total = 0.0f;

        for (CartItem cartItem : cartItems) {
            total += cartItem.getSubTotal();
        }

        return total;
    }

    private float calculateProductCost(List<CartItem> cartItems) {
        float cost = 0.0f;
        for (CartItem cartItem : cartItems) {
            cost += cartItem.getProduct().getCost() * cartItem.getQuantity();
        }

        return cost;
    }
}
