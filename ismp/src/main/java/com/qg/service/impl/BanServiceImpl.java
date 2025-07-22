package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Ban;
import com.qg.mapper.BanMapper;
import com.qg.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BanServiceImpl implements BanService {

    @Autowired
    private BanMapper banMapper;

    @Override
    public List<Ban> selectAll() {
        LambdaQueryWrapper<Ban> wrapper = new LambdaQueryWrapper<>();
        wrapper.orderByDesc(Ban::getId);
        return banMapper.selectList(wrapper);
    }
}
