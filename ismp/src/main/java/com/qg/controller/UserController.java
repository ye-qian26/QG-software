package com.qg.controller;


import com.qg.domain.Order;
import com.qg.domain.Result;
import com.qg.domain.User;
import com.qg.service.OrderService;
import com.qg.service.UserService;
import jakarta.websocket.server.PathParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.qg.domain.Code.CONFLICT;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

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

    @PostMapping("/updata")
    public Result update(@RequestBody User user) {
        return userService.update(user);
    }

    @PostMapping("/delete")
    public Result delete(@RequestParam Integer id) {
        return userService.delete(id);
    }

    @PostMapping("/buy")
    public Result buy(@RequestBody Order order) {
        long userId = order.getUserId();
        long authorId = order.getDeveloperId();
        double price = order.getPrice();
        long softwareId = order.getSoftwareId();


        int transaction = userService.transaction(userId, authorId, price);

        int orderSave = orderService.saveOrder(order);

        // equipmentSave =

        if (transaction <= 0 || orderSave <= 0) {
            return new Result(CONFLICT,"交易失败,请稍后再试！");
        }
        return null;

    }
}
