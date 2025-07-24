package com.qg.controller;


import com.qg.domain.Equipment;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.EquipmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.List;

import static com.qg.domain.Code.*;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    /**
     * 获取用户的所有已购买软件
     * @param userId
     * @return
     */
    @GetMapping("/selectPurchased/{userId}")
    public Result selectPurchased(@PathVariable Long userId) {
        if (userId <= 0) {
            return new Result(BAD_REQUEST, "查看错误");
        }

        List<Software> softwareList = equipmentService.selectPurchased(userId);


        if (softwareList == null || softwareList.isEmpty()) {
            new Result(NOT_FOUND, "数据错误");
        }

        return new Result(SUCCESS, softwareList, "查看成功");
    }


    /**
     * 查看某个软件是否已经买了
     * @param equipment
     * @return
     */
    @GetMapping("/isPurchased")
    public Result isPurchased(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();


        if (userId <= 0 || softwareId <= 0) {
            return new Result(BAD_REQUEST, false, "查看错误");
        }
        boolean isPurchased = equipmentService.isPurchased(userId, softwareId);

        return new Result(SUCCESS, isPurchased, "查询成功");
    }

    /**
     * 获取用户的所有已预约软件
     * @param userId
     * @return
     */
    @GetMapping("/selectAppointment/{userId}")
    public Result selectAppointment(@PathVariable Long userId) {
        if (userId <= 0) {
            return new Result(BAD_REQUEST, "查看错误");
        }
        List<Software> softwareList = equipmentService.selectAppointment(userId);

        return new Result(SUCCESS, softwareList, "查询成功");
    }

    /**
     * 查看是否预购
     * @param equipment
     * @return
     */
    @GetMapping("/isAppointment")
    public Result isAppointment(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();


        if (userId <= 0 || softwareId <= 0) {
            return new Result(BAD_REQUEST, false, "查看错误");
        }
        boolean isAppointment = equipmentService.isAppointment(userId, softwareId);

        return new Result(SUCCESS, isAppointment, "查询成功");
    }

    /**
     * 用户预约软件
     * @param equipment
     * @return
     */
    @PostMapping("/addAppointment")
    public Result addAppointment(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();
        if (userId <= 0 || softwareId <= 0) {
            return new Result(BAD_REQUEST, "查看错误");
        }
        int addAppointment = equipmentService.addAppointment(equipment);

        return addAppointment > 0 ? new Result(SUCCESS, "预约成功") : new Result(INTERNAL_ERROR, "预约失败，请稍后重试");
    }


    /**
     * 管理员看所有用户的预约软件
     * @return
     */
    @GetMapping("/findAllAppointment")
    public Result findAllAppointment() {
        List<Software> softwareList = equipmentService.adminGetAllOrderSoftware();
        return softwareList != null ? new Result(SUCCESS, softwareList, "查看成功") : new Result(INTERNAL_ERROR, "查看失败，请稍后重试");
    }


    @PutMapping("/update")
    public Result update(@RequestBody Equipment equipment) {
        boolean flag = equipmentService.updateCode(equipment);
        return flag ? new Result(SUCCESS,"修改成功") : new Result(BAD_GATEWAY,"修改失败");
    }

    @PostMapping("/addNetWorkCode")
    public Result addNetWorkCode(@RequestBody Equipment equipment) throws SocketException, UnknownHostException {
        boolean flag = equipmentService.addNetWorkCode(equipment);
        return flag ? new Result(SUCCESS,"绑定成功") : new Result(BAD_GATEWAY,"绑定失败");
    }




}
