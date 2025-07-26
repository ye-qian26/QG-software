package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.domain.*;

import com.qg.mapper.*;
import com.qg.service.MessageService;
import com.qg.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.util.List;


@Service
public class MessageServiceImpl implements MessageService {

    @Autowired
    private MessageMapper messageMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private SoftwareMapper softwareMapper;
    @Autowired
    private SubscribeMapper subscribeMapper;
    @Autowired
    private UserMapper userMapper;

    /**
     * @param software 管理员修改软件信息后，通知开发商
     */
    @Override
    public boolean adminUpdateSoftwareInformation(Software software) {
        return messageMapper.insert(new Message(
                null, software.getAuthorId()
                , 1L, "管理员修改了您的软件信息，被修改的软件为：" +
                software.getName() + software.getVersion() +
                "，修改后的内容如下：\n" + software.getInfo(),
                null, Constants.MESSAGE_NO_READ)
        ) > 0;
    }

    /**
     * @param userId 查看某个用户是否有未读信息
     */
    @Override
    public List<Message> checkIfHaveNewMessage(Long userId) {
        return messageMapper.selectList(
                new QueryWrapper<Message>()
                        .eq("receiver_id", userId)
                        .eq("is_read", Constants.MESSAGE_NO_READ)
                        .orderByDesc("time")
        );
    }

    /**
     * @param userId 获取某个用户所有信息
     */
    @Override
    public List<Message> getAllMessage(Long userId) {
        return messageMapper.selectList(
                new QueryWrapper<Message>()
                        .eq("receiver_id", userId)
                        .orderByDesc("time")
        );
    }

    /**
     * @param userId
     * @param messageId 标记信息为已读
     */
    @Override
    public boolean read(Long userId, Long messageId) {
        return messageMapper.read(userId, messageId) > 0;
    }

    /**
     * @param userId 删除所有已读信息
     */
    @Override
    public boolean deleteAllRead(Long userId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        // 筛选该用户id下所有已读信息
        queryWrapper.eq("receiver_id", userId)
                .eq("is_read", Constants.MESSAGE_READ);
        return messageMapper.delete(queryWrapper) > 0;
    }

    /**
     * 成功
     * 申请成为开发者通知
     *
     * @param userId
     * @return
     */
    @Override
    public boolean applyDeveloperSuccess(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            return false;
        }
        user.setRole(Constants.USER_ROLE_DEVELOPER);
        return userMapper.updateById(user) > 0 &&
                messageMapper.insert(new Message(
                null, userId
                , 1L, "恭喜您，申请成为开发者成功！"
                , null, Constants.MESSAGE_NO_READ)
        ) > 0;
    }

    /**
     * 失败
     * 申请成为开发者通知
     *
     * @param userId
     * @param reason
     * @return
     */
    @Override
    public boolean applyDeveloperFailure(Long userId, String reason) {
        if (userMapper.selectById(userId) == null) {
            return false;
        }
        return messageMapper.insert(new Message(
                null, userId
                , 1L, "很遗憾，申请成为开发者失败！管理员驳回理由如下：\n"
                + reason, null, Constants.MESSAGE_NO_READ)
        ) > 0;
    }

    /**
     * @param applySoftware 成功
     *                      发布软件通知
     */
    @Override
    public boolean applySoftwareSuccess(ApplySoftware applySoftware) {
        Software software = softwareMapper.selectById(applySoftware.getSoftwareId());
        if (userMapper.selectById(applySoftware.getUserId()) == null) {
            return false;
        }
        return messageMapper.insert(new Message(
                null, applySoftware.getUserId()
                , 1L, "恭喜您，申请发布软件："
                + software.getName() + "，版本号："
                + software.getVersion() + "成功！"
                , null, Constants.MESSAGE_NO_READ)
        ) > 0;
    }

    /**
     * @param applySoftware 失败
     *                      发布软件通知
     */
    @Override
    public boolean applySoftwareFailure(ApplySoftware applySoftware) {
        Software software = softwareMapper.selectById(applySoftware.getSoftwareId());
        if (userMapper.selectById(applySoftware.getUserId()) == null) {
            return false;
        }
        return messageMapper.insert(new Message(
                null, applySoftware.getUserId()
                , 1L, "很遗憾，申请发布软件："
                + software.getName() + "，版本号："
                + software.getVersion() + "失败！管理员驳回理由如下：\n"
                + applySoftware.getReason(), null, Constants.MESSAGE_NO_READ)
        ) > 0;
    }

    /**
     * @param softwareId 上市
     *                   软件上市通知
     */
    @Override
    public void orderSoftwareLaunch(Long softwareId) {
        // 获取所有预约该软件的信息
        List<Equipment> orderList = equipmentMapper.selectList(
                new QueryWrapper<Equipment>()
                        .eq("software_id", softwareId)
        );

        // 获取该软件作者信息
        Software software = softwareMapper.selectById(softwareId);
        User user = userMapper.selectById(software.getAuthorId());

        // 获取关注该软件作者的所有用户
        List<Subscribe> subscribeList = subscribeMapper.selectList(
                new QueryWrapper<Subscribe>()
                        .eq("developer_id", software.getAuthorId())
        );

        if (!orderList.isEmpty()) {
            // 给所有预约该软件的用户发信息
            orderList.forEach(equipment -> {
                        messageMapper.insert(new Message(
                                null, equipment.getUserId(), software.getAuthorId(),
                                "您关注的作者" + user.getName()
                                        + " ,版本号：" + software.getVersion()
                                        + "已上市，快去购买吧！",
                                null, Constants.MESSAGE_NO_READ)
                        );
                    }
            );

        }

        if (user != null && !subscribeList.isEmpty()) {
            // 给所有关注该软件的用户发信息
            subscribeList.forEach(subscribe -> {
                        messageMapper.insert(new Message(
                                null, subscribe.getUserId(), software.getAuthorId(),
                                "您关注的开发商" + user.getName() + "有新的产品发布",
                                null, Constants.MESSAGE_NO_READ)
                        );
                    }
            );
        }
    }
}




