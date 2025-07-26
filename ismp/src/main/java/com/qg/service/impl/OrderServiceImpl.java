package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
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
        Long count = orderMapper.selectCount(
                new QueryWrapper<Order>()
                        .eq("user_id", order.getUserId())
                        .eq("software_id", order.getSoftwareId())
        );

        if (count > 0) {
            // 已存在相同记录，不插入
            return 0;
        }

        // 不存在则插入新记录
        return orderMapper.insert(order);
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
