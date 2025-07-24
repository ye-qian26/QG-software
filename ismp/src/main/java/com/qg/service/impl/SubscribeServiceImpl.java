package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;

import com.qg.utils.Constants;
import com.qg.domain.Subscribe;
import com.qg.domain.User;
import com.qg.mapper.SubscribeMapper;
import com.qg.mapper.UserMapper;
import com.qg.service.SubscribeService;
import com.qg.vo.SubscribeVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class SubscribeServiceImpl implements SubscribeService {

    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private UserMapper userMapper;


    /**
     * 用户关注某个开发商
     */
    @Override
    public boolean subscribe(Subscribe subscribe) {
        // 检查关注的开发者是否存在、是否真的是开发者
        User developer = userMapper.selectById(subscribe.getDeveloperId());
        if (developer == null || !developer.getRole().equals(Constants.USER_ROLE_DEVELOPER)) {
            return false;
        }

        // 检查是否已关注
        QueryWrapper<Subscribe> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", subscribe.getUserId())
                .eq("developer_id", subscribe.getDeveloperId());

        // 已关注，直接返回false; 未关注，执行关注操作
        return subscribeMapper.selectCount(queryWrapper) <= 0
                && subscribeMapper.insert(subscribe) > 0;
    }

    /**
     * 用户取消关注某个开发商
     */
    @Override
    public boolean unsubscribe(Subscribe subscribe) {
        // 检查是否为空
        if (subscribe == null || subscribe.getUserId() == null || subscribe.getDeveloperId() == null) {
            return false;
        }

        // 检查是否已关注
        QueryWrapper<Subscribe> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", subscribe.getUserId())
                .eq("developer_id", subscribe.getDeveloperId());

        // 执行取消关注操作
        return subscribeMapper.selectCount(queryWrapper) > 0
                && subscribeMapper.delete(queryWrapper) > 0;
    }

    /**
     * 用户查看所有关注的开发商
     */
    @Override
    public List<SubscribeVO> getMySubscribe(Long userId) {
        return subscribeMapper.getMySubscribe(userId);
    }



    @Override
    public boolean isSubscribe(Long userId, Long developerId) {
        LambdaQueryWrapper<Subscribe> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Subscribe::getUserId, userId).eq(Subscribe::getDeveloperId, developerId);

        Subscribe subscribe = subscribeMapper.selectOne(queryWrapper);

        if (subscribe == null) {
            return false;
        }

        return true;
    }

}
