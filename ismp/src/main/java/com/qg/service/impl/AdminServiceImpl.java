package com.qg.service.impl;

import com.qg.mapper.BanMapper;
import com.qg.mapper.UserMapper;
import com.qg.service.AdminService;
import com.qg.vo.AdminManageUserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private UserMapper userMapper;


    /**
     * 获取所有用户信息
     * @return
     */
    @Override
    public List<AdminManageUserVO> getAllUser() {
        return userMapper.getAllUser();
    }
}
