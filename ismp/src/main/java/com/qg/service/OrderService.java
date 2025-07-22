package com.qg.service;

import com.qg.domain.Order;

import java.util.List;


public interface OrderService {
    int saveOrder(Order order);

    List<Order> findAllByUserId(Long id);

    List<Order> selectAll();
}
