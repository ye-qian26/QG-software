package com.qg.service.impl;

import com.qg.domain.Order;
import com.qg.mapper.OrderMapper;
import com.qg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int saveOrder(Order order) {

        return  orderMapper.insert(order);
    }
}
