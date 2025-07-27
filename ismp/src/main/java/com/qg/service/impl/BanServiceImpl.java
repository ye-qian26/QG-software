package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Ban;
import com.qg.mapper.BanMapper;
import com.qg.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Override
    public boolean add(Ban ban) {
        if (judgeBan(ban.getUserId())) {
            return false;
        } else {
            LambdaQueryWrapper<Ban> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Ban::getUserId, ban.getUserId());
            banMapper.delete(wrapper);
        }
        return banMapper.insert(ban) > 0;
    }

    @Override
    public boolean delete(Long userId) {
        LambdaQueryWrapper<Ban> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ban::getUserId, userId);
        return banMapper.delete(wrapper) > 0;
    }

    @Override
    public Ban selectByUserId(Long userId) {
        LambdaQueryWrapper<Ban> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ban::getUserId, userId);
        return banMapper.selectOne(wrapper);
    }

    @Override
    public boolean judgeBan(Long userId) {
        LambdaQueryWrapper<Ban> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Ban::getUserId, userId);
        Ban ban = banMapper.selectOne(wrapper);
        if (ban == null) {
            return false;
        } else {
            String endTime = ban.getEndTime();
            // 解析为 LocalDateTime
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dbDateTime = LocalDateTime.parse(endTime, formatter);
            // 获取当前时间
            LocalDateTime nowDateTime = LocalDateTime.now();
            return dbDateTime.isAfter(nowDateTime);
        }
    }
}
