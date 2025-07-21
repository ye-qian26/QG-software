package com.qg.controller;


import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Subscribe;
import com.qg.domain.User;
import com.qg.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/subscribes")
public class SubscribeController {

    @Autowired
    private SubscribeService subscribeService;

    /**
     * 获取所有已关注的开发者信息
     */
    @GetMapping("/{user_id}")
    public Result getAllSubscribe(@PathVariable("user_id") Long userId) {
        System.out.println("=>id为：" + userId + " 的用户，获取全部开发者信息");
        List<User> allSubscribeList = subscribeService.getAllSubscribe(userId);
        if (allSubscribeList != null) {
            System.out.println("==>获取全部开发者信息成功！");
            return new Result(Code.SUCCESS, allSubscribeList, "获取成功!");
        } else {
            return new Result(Code.NOT_FOUND, "您还没有关注任何开发者！");
        }
    }

    /**
     * 关注某个开发者
     */
    @PostMapping
    public Result insertSubscribe(@RequestBody Subscribe subscribe) {
        if (subscribeService.subscribe(subscribe)) {
            System.out.println("==>关注成功！");
            return new Result(Code.SUCCESS, "关注成功!");
        } else {
            return new Result(Code.BAD_REQUEST, "开发者不存在或已关注！");
        }
    }
}
