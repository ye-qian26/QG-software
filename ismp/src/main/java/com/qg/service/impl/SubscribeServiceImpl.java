package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.domain.Constants;
import com.qg.domain.Subscribe;
import com.qg.domain.User;
import com.qg.mapper.SubscribeMapper;
import com.qg.mapper.UserMapper;
import com.qg.service.SubscribeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public List<User> getAllSubscribe(Long userId) {
        List<Subscribe> subscribeList = subscribeMapper.selectList(
                // 查找关注开发者的id
                new QueryWrapper<Subscribe>().eq("user_id", userId)
        );

        // 提取所有被关注用户的ID
        List<Long> developerIds = subscribeList.stream()
                .map(Subscribe::getDeveloperId)
                .collect(Collectors.toList());

        // 查询这些开发者的所有信息
        return userMapper.selectBatchIds(developerIds);

    }

}
