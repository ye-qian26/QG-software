package com.qg.controller;



import cn.hutool.core.bean.BeanUtil;
import com.qg.domain.Ban;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.User;

import com.qg.dto.UserDto;
import com.qg.service.BanService;
import com.qg.service.UserService;
import com.qg.utils.EmailService;
import com.qg.utils.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static com.qg.domain.Code.*;


import static com.qg.domain.Code.SUCCESS;



@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private BanService banService;

    @Autowired
    private EmailService emailService;


    @GetMapping("/password")
    public Result loginByPassword(@RequestParam String email, @RequestParam String password) {
        Map<String,Object> map = userService.loginByPassword(email, password);
        User user = (User) map.get("user");
        if (user == null) {
            return new Result(BAD_REQUEST,"未注册");
        }
        Long id = user.getId();
        if (banService.judgeBan(id)) {
            Ban ban = banService.selectByUserId(id);
            return new Result(FORBIDDEN, ban, "账号被封禁，无法登录");
        }
        map.put("user", BeanUtil.copyProperties(user, UserDto.class));
        return new Result(SUCCESS, map ,"登录成功");
    }

    @GetMapping("/code")
    public Result loginByCode(@RequestParam String email, @RequestParam String code) {
        userService.loginByCode(email, code);
        return null;
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user) {
        return userService.register(user);
    }

    @PutMapping("/update")
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

    @GetMapping("/getInformation/{id}")
    public Result getInformation(@PathVariable Long id) {
        User user = userService.getUser(id);
        if (user == null ) {
            return  new Result(BAD_GATEWAY,"获取失败");
        }
        return new Result(SUCCESS,user,"获取成功");
    }

    @PostMapping("/sendCodeByEmail")
    public Result sendCodeByEmail(@RequestParam("email") String email) {
        System.out.println(email);
        // 发送验证码到邮箱
        return userService.sendCodeByEmail(email);
    }
}
