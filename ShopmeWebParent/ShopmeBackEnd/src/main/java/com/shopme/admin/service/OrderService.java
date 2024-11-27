package com.shopme.admin.service;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.common.entity.Order;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.data.domain.Page;

public interface OrderService {
    Page<Order> listByPage(Integer pageNum, PagingAndSortingHelper helper);

    Order getOrderById(Integer orderId) throws ResourceNotFoundException;

    void deleteOrderById(Integer orderId);
}
