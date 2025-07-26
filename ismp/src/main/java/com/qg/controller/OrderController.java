package com.qg.controller;


import com.qg.domain.Equipment;
import com.qg.domain.Order;
import com.qg.domain.Result;

import com.qg.service.*;
import com.qg.utils.Constants;
import com.qg.utils.NetWorkCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.net.SocketException;
import java.net.UnknownHostException;
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

    @Autowired
    private SoftwareSearchService softwareSearchService;


    /**
     * 用户发起交易
     * @param order
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @PostMapping("/buy")
    public Result buy(@RequestBody Order order) throws Exception {
        long userId = order.getUserId();
        long authorId = order.getDeveloperId();
        double price = order.getPrice();
        long softwareId = order.getSoftwareId();

        Integer status = Constants.EQUIPMENT_STATUS_BOUGHT;

        String name = softwareSearchService.SearchSoftware(softwareId).getName();


        Equipment equipment = new Equipment(userId, softwareId, status, name);

        System.out.println(equipment);

        int transaction = userService.transaction(userId, authorId, price);
        if (transaction <= 0) {
            System.out.println("失败");
            throw new SocketException("交易失败,请稍后再试！");
        }

        int orderSave = orderService.saveOrder(order);
        if (orderSave <= 0) {
            throw new UnknownHostException("订单保存失败,请稍后再试！");
        }

        int equipmentSave = equipmentService.saveEquipment(equipment);
        if (equipmentSave <= 0) {
            throw new SocketException("设备保存失败,请稍后再试！");
        }
        return new Result(SUCCESS, "交易成功！");
    }


    /**
     * 用户查找自己所有的订单
     * @param id
     * @return
     */
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


    /**
     * 查看购买的订单
     * @return
     */
    @GetMapping("/findAll")
    public Result findAll() {
        List<Order> orderList = orderService.selectAll();
        return orderList!=null ? new Result(SUCCESS,orderList,"查询成功") : new Result(BAD_GATEWAY,"查询失败");
    }
}
