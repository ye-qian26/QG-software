package com.qg.controller;



import com.qg.domain.Result;
import com.qg.domain.User;

import com.qg.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.qg.domain.Code.*;


import static com.qg.domain.Code.SUCCESS;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;




    @GetMapping("/password")
    public Result loginByPassword(@RequestParam String email, @RequestParam String password) {
        return userService.loginByPassword(email, password);
    }

    @GetMapping("/code")
    public Result loginByCode(@RequestParam String email, @RequestParam String code) {
        return userService.loginByCode(email, code);
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/updata")
    public Result update(@RequestBody User user) {
        return userService.update(user);
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Long id) {
        return userService.delete(id);
    }

    @GetMapping("/getPrice/{id}")
    public Result getPriceById(@PathVariable Long id) {
        if (id <= 0) {
            return new Result(BAD_REQUEST, "获取失败！");
        }
        double price = userService.getPriceById(id);

        return new Result(SUCCESS, String.valueOf(price), "获取成功！");
    }

}
