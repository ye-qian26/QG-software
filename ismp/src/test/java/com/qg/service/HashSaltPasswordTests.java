package com.qg.service;

import com.qg.domain.User;
import com.qg.mapper.UserMapper;
import com.qg.utils.HashSaltUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class HashSaltPasswordTests {

    @Autowired
    private UserMapper userMapper;

    @Test
    public void setAllPasswordToHashSalt() {
        List<User> userList = userMapper.selectList(null);
        for (User user : userList) {
            if (!HashSaltUtil.isPlainPassword(user.getPassword())) return;
            user.setPassword(HashSaltUtil.creatHashPassword(user.getPassword()));
            userMapper.updateById(user);
        }
        System.out.println(userList);
    }
}
