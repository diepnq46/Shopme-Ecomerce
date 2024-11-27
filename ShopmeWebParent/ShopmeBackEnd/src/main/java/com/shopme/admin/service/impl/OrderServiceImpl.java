package com.shopme.admin.service.impl;

import com.shopme.admin.paging.PagingAndSortingHelper;
import com.shopme.admin.repository.OrderRepository;
import com.shopme.admin.service.OrderService;
import com.shopme.common.entity.Order;
import com.shopme.common.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    public static final int ORDERS_PER_PAGE = 10;

    @Autowired
    private OrderRepository repo;

    @Override
    public Page<Order> listByPage(Integer pageNum, PagingAndSortingHelper helper) {
        String sortDir = helper.getSortDir();
        String sortField = helper.getSortField();
        String keyword = helper.getKeyword();

        Sort sort = null;
        if("destination".equals(sortField)) {
            sort = Sort.by("country").and(Sort.by("state").and(Sort.by("city")));
        }else {
            sort = Sort.by(sortField);
        }
        sort = "asc".equals(sortDir) ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, ORDERS_PER_PAGE, sort);

        if (keyword != null) {
            return repo.search(keyword, pageable);
        }
        return repo.findAll(pageable);
    }

    @Override
    public Order getOrderById(Integer orderId) throws ResourceNotFoundException {
        return repo.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng có ID: " + orderId));
    }

    @Override
    public void deleteOrderById(Integer orderId) {
        repo.deleteById(orderId);
    }
}
