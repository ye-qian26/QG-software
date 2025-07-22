package com.qg.controller;


import com.qg.domain.Equipment;
import com.qg.domain.Order;
import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.service.EquipmentService;
import com.qg.service.OrderService;
import com.qg.service.UserService;
import jakarta.websocket.server.PathParam;
import org.apache.ibatis.annotations.Delete;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.qg.domain.Code.*;
import static com.qg.domain.Constants.EQUIPMENT_STATUS_BOUGHT;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private EquipmentService equipmentService;

    @GetMapping("/password")
    public Result loginByPassword(@RequestParam String email, @RequestParam String password) {
        return userService.loginByPassword(email,password);
    }

    @GetMapping("/code")
    public Result loginByCode(@RequestParam String email, @RequestParam String code) {
        return userService.loginByCode(email,code);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/updata")
    public Result update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return userService.delete(id);
    }

    @PostMapping("/buy")
    public Result buy(@RequestBody Order order) {
        long userId = order.getUserId();
        long authorId = order.getDeveloperId();
        double price = order.getPrice();
        long softwareId = order.getSoftwareId();

        Integer status = EQUIPMENT_STATUS_BOUGHT;
        Equipment equipment = new Equipment(userId,softwareId,status);


        int transaction = userService.transaction(userId, authorId, price);

        int orderSave = orderService.saveOrder(order);

        int equipmentSave = equipmentService.saveEquipment(equipment);

        if (transaction <= 0 || orderSave <= 0 || equipmentSave <= 0) {
            return new Result(CONFLICT,"交易失败,请稍后再试！");
        }

        return new Result(SUCCESS, "交易成功！");
    }

    @GetMapping("/getPrice")
    public Result getPriceById(@RequestParam Long id) {
        if (id <= 0){
            return new Result(BAD_REQUEST,"获取失败！");
        }
        double price = userService.getPriceById(id);

        return new Result(SUCCESS,String.valueOf(price),"获取成功！");
    }
}
