package com.qg.controller;


import com.qg.domain.*;
import com.qg.service.MessageService;
import com.qg.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("/messages")
public class MessageController {

    @Autowired
    private MessageService messageService;


    /**
     * 管理员修改软件信息后
     * 通知开发商
     */
    @PostMapping("/adminUpdateInfo")
    public Result adminUpdateSoftwareInformation(@RequestBody Software software) {
        if (software == null) {
            return new Result(Code.BAD_REQUEST, "软件信息为空");
        }
        if (messageService.adminUpdateSoftwareInformation(software)) {
            return new Result(Code.SUCCESS, "已通知id为:"
                    + software.getAuthorId() + "的开发者");
        } else {
            return new Result(Code.NOT_FOUND, "开发者不存在");
        }
    }

    /**
     * 用户获取所有通知信息
     */
    @GetMapping("/{userId}")
    public Result getAllMessage(@PathVariable("userId") Long userId) {
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "用户不存在");
        }
        System.out.println("=>id为：" + userId + " 的用户，获取全部通知信息");
        List<Message> messageList = messageService.getAllMessage(userId);
        if (!messageList.isEmpty()) {
            System.out.println("==>获取全部通知信息成功！");
            return new Result(Code.SUCCESS, messageList, "获取成功!");
        } else {
            return new Result(Code.NOT_FOUND, "目前还没有通知信息！");
        }
    }

    /**
     * 用户查看是否有新的通知信息
     */
    @GetMapping("/check/{user_id}")
    public Result checkIfHaveNewMessage(@PathVariable("user_id") Long userId) {
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "用户不存在");
        }
        System.out.println("=>id为：" + userId + " 的用户，查看是否有新的通知信息");
        List<Message> messageList = messageService.checkIfHaveNewMessage(userId);
        if (!messageList.isEmpty()) {
            System.out.println("==>有新的通知！");
            return new Result(Code.SUCCESS, messageList, "有新的通知！");
        } else {
            return new Result(Code.NOT_FOUND, "没有新的通知");
        }
    }

    /**
     * 用户标记某条信息为已读
     */
    @PutMapping
    public Result read(@RequestBody Message message) {
        if (message == null) {
            return new Result(Code.BAD_REQUEST, "消息不存在");
        }
        System.out.println(message);
        if (Objects.equals(message.getIsRead(), Constants.MESSAGE_NO_READ)
                && messageService.read(message.getReceiverId(), message.getId())) {
            return new Result(Code.SUCCESS, "消息标记为已读！");
        } else {
            return new Result(Code.NOT_FOUND, "消息不存在或已读");
        }
    }

    /**
     * 删除所有已读信息
     */
    @DeleteMapping("/{userId}")
    public Result deleteAllRead(@PathVariable("userId") Long userId) {
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "用户不存在");
        }
        if (messageService.deleteAllRead(userId)) {
            return new Result(Code.SUCCESS, "删除成功！");
        } else {
            return new Result(Code.NOT_FOUND, "无已读消息");
        }
    }

    /**
     * 成功
     * 申请成为开发者通知
     */
    @PostMapping("/applyDeveloper/1")
    public Result applyDeveloperSuccess(@RequestBody ApplyDeveloper applyDeveloper) {
        if (applyDeveloper == null) {
            return new Result(Code.BAD_REQUEST, "申请为空");
        }
        if (messageService.applyDeveloperSuccess(applyDeveloper.getUserId())) {
            return new Result(Code.SUCCESS, "已通知id为:"
                    + applyDeveloper.getUserId() + "的用户——申请成功");
        } else {
            return new Result(Code.NOT_FOUND, "申请者不存在");
        }
    }

    /**
     * 失败
     * 申请成为开发者通知
     */
    @PostMapping("/applyDeveloper/0")
    public Result applyDeveloperFailure(@RequestBody ApplyDeveloper applyDeveloper) {
        if (applyDeveloper == null) {
            return new Result(Code.BAD_REQUEST, "申请为空");
        }
        if (messageService.applyDeveloperFailure(applyDeveloper.getUserId(), applyDeveloper.getReason())) {
            return new Result(Code.SUCCESS, "已通知id为:"
                    + applyDeveloper.getUserId() + "的用户——申请失败");
        } else {
            return new Result(Code.NOT_FOUND, "申请者不存在");
        }
    }

    /**
     * 成功
     * 申请发布软件通知
     */
    @PostMapping("/applySoftware/1")
    public Result applySoftwareSuccess(@RequestBody ApplySoftware applySoftware) {
        if (applySoftware == null) {
            return new Result(Code.BAD_REQUEST, "申请为空");
        }
        if (messageService.applySoftwareSuccess(applySoftware)) {
            return new Result(Code.SUCCESS, "已通知id为:"
                    + applySoftware.getUserId() + "的用户——申请成功");
        } else {
            return new Result(Code.NOT_FOUND, "申请者不存在");
        }
    }

    /**
     * 失败
     * 申请发布软件通知
     */
    @PostMapping("/applySoftware/0")
    public Result applySoftwareFailure(@RequestBody ApplySoftware applySoftware) {
        if (applySoftware == null) {
            return new Result(Code.BAD_REQUEST, "申请为空");
        }
        if (messageService.applySoftwareFailure(applySoftware)) {
            return new Result(Code.SUCCESS, "已通知id为:"
                    + applySoftware.getUserId() + "的用户——申请失败");
        } else {
            return new Result(Code.NOT_FOUND, "申请者不存在");
        }
    }

    /**
     * 发布软件
     * 通知所有预约的用户
     * 关注该开发商的用户
     */
    @PostMapping("/launch/{softwareId}")
    public Result orderSoftwareLaunch(@PathVariable("softwareId") Long softwareId) {
        if (softwareId == null) {
            return new Result(Code.BAD_REQUEST, "软件id为空");
        }
        messageService.orderSoftwareLaunch(softwareId);
        return new Result(Code.SUCCESS, "已通知预约用户和关注用户");
    }

}
