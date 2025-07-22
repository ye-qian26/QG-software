package com.qg.controller;


import com.qg.domain.Equipment;
import com.qg.domain.Order;
import com.qg.domain.Result;

import com.qg.service.EquipmentService;
import com.qg.service.OrderService;
import com.qg.service.UserService;
import com.qg.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

import static com.qg.domain.Code.*;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private EquipmentService equipmentService;

    @Autowired
    private UserService userService;



    @PostMapping("/buy")
    public Result buy(@RequestBody Order order) {
        long userId = order.getUserId();
        long authorId = order.getDeveloperId();
        double price = order.getPrice();
        long softwareId = order.getSoftwareId();

        Integer status = Constants.EQUIPMENT_STATUS_BOUGHT;
        Equipment equipment = new Equipment(userId, softwareId, status);


        int transaction = userService.transaction(userId, authorId, price);

        int orderSave = orderService.saveOrder(order);

        int equipmentSave = equipmentService.saveEquipment(equipment);


        if (transaction <= 0 || orderSave <= 0 || equipmentSave <= 0) {
            return new Result(CONFLICT, "交易失败,请稍后再试！");
        }

        return new Result(SUCCESS, "交易成功！");
    }



    @GetMapping("/findAllById/{id}")
    public Result findAllByUserId(@PathVariable Long id) {
        if (id <= 0) {
            new Result(BAD_REQUEST, "查询失败");
        }
        List<Order> orders = orderService.findAllByUserId(id);


        if (orders == null) {
            return new Result(NOT_FOUND, "订单加载失败！");
        }
        if (orders.size() == 0) {
            return new Result(NOT_FOUND,null,"尚未有订单");
        }

        return new Result(SUCCESS, orders, "订单查询成功！");
    }
}
