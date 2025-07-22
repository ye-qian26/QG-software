package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Order;
import com.qg.mapper.OrderMapper;
import com.qg.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Override
    public int saveOrder(Order order) {

        return  orderMapper.insert(order);
    }

    @Override
    public List<Order> findAllByUserId(Long id) {
        LambdaQueryWrapper<Order> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Order::getUserId, id);
        List<Order> orders =  orderMapper.selectList(wrapper);
        return orders;
    }

    @Override
    public List<Order> selectAll() {
        return  orderMapper.selectList(null);
    }
}
