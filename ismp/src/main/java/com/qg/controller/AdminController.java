package com.qg.controller;

import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.AdminService;
import com.qg.utils.Constants;
import com.qg.vo.AdminManageUserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admins")
public class AdminController {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    @Autowired
    private AdminService adminService;


    /**
     * 验证管理员身份
     *
     * @param token
     * @return
     */
    public boolean identify(String token) {
        String redisKey = "login:user:" + token;
        String role = (String) stringRedisTemplate.opsForHash().get(redisKey, "role");

        System.out.println("当前用户token" + redisKey + "\n身份" + role);
        try {
            return role != null && role.equals(Constants.USER_ROLE_ADMIN);
        } catch (NumberFormatException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }


    /**
     * 获取所有用户信息
     * @param request
     * @return
     */
    @GetMapping
    public Result getAllUser(HttpServletRequest request) {
//        String token = request.getHeader("Authorization");
//        if (!identify(token)) {
//            return new Result(Code.FORBIDDEN, "权限不足");
//        }
        List<AdminManageUserVO> list = adminService.getAllUser();
        if (list == null || list.isEmpty()) {
            return new Result(Code.NOT_FOUND, "当前无用户信息");
        } else {
            return new Result(Code.SUCCESS, list, "获取用户信息成功");
        }

    }


    /**
     * 根据名字模糊查询用户
     * @param request
     * @param name
     * @return
     */
    @GetMapping("/name")
    public Result getUserByName(HttpServletRequest request, @RequestParam String name) {
        String token = request.getHeader("Authorization");
        if (!identify(token)) {
            return new Result(Code.FORBIDDEN, "权限不足");
        }
        List<AdminManageUserVO> list = adminService.getUserByName(name);
        if (list == null || list.isEmpty()) {
            return new Result(Code.NOT_FOUND, "当前无用户信息");
        } else {
            return new Result(Code.SUCCESS, list, "获取用户信息成功");
        }

    }



}
