package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Equipment;
import com.qg.domain.User;
import com.qg.mapper.EquipmentMapper;
import com.qg.mapper.UserMapper;
import com.qg.service.ReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ReceiveServiceImpl implements ReceiveService {
    @Autowired
    private UserMapper userMapper;

    @Autowired
    private EquipmentMapper equipmentMapper;


    @Override
    public String Permissions(String data) {

        if (data == null || !data.contains("mac=") || !data.contains("email=") || !data.contains("name=")) {
            System.out.println("信息不全");
            return "false";
        }

        String[] parts = data.split(";");
        if (parts.length < 3) {
            System.out.println("长度不够");
            return "false";
        }

        String mac = parts[0].split("=", 2)[1];
        String email = parts[1].split("=", 2)[1];
        String name = parts[2].split("=", 2)[1];

        LambdaQueryWrapper<User> queryWrapper1 = new LambdaQueryWrapper<>();
        queryWrapper1.eq(User::getEmail, email);
        System.out.println(mac);
        System.out.println(email);
        User user = userMapper.selectOne(queryWrapper1);

        if (user == null) {
            System.out.println("没有这邮箱对应的用户");
            return "false";
        }

        System.out.println("用户信息：" + user);

        LambdaQueryWrapper<Equipment> queryWrapper2 = new LambdaQueryWrapper<>();
        queryWrapper2.eq(Equipment::getUserId, user.getId());
        queryWrapper2.eq(Equipment::getName, name);
        List<Equipment> equipmentList = equipmentMapper.selectList(queryWrapper2);


        for (Equipment equipment : equipmentList) {
            System.out.println(equipment);
            if (mac.equals(equipment.getCode1()) || mac.equals(equipment.getCode2()) || mac.equals(equipment.getCode3())) {
                return "true";
            }
        }


        System.out.println("未知错误");
        return "false";
    }
}
