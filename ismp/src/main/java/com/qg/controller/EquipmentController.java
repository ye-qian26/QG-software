package com.qg.controller;


import com.qg.domain.Equipment;
import com.qg.domain.Result;
import com.qg.service.EquipmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


import java.util.List;

import static com.qg.domain.Code.*;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    @Autowired
    private EquipmentService equipmentService;


    //查看买了什么软件
    @GetMapping("/selectPurchased/{userId}")
    public Result selectPurchased(@PathVariable Long userId) {
        if (userId <= 0){
            return new Result(BAD_REQUEST, "查看错误");
        }

        List<Equipment> equipmentList = equipmentService.selectPurchased(userId);

        if (equipmentList == null || equipmentList.size() == 0){
            new Result(NOT_FOUND, "数据错误");
        }

        return new Result(SUCCESS,equipmentList,"查看成功") ;
    }


    //查看某个软件是否已经买了
    @GetMapping("/isPurchased")
    public Result isPurchased(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();


        if (userId <= 0 || softwareId <= 0 ) {
            return new Result(BAD_REQUEST, false,"查看错误");
        }
        boolean isPurchased = equipmentService.isPurchased(userId, softwareId);

        return new Result(SUCCESS, isPurchased, "查询成功") ;
    }

    //查看预约
    @GetMapping("/selectAppointment/{userId}")
    public Result selectAppointment(@PathVariable Long userId) {
        if (userId <= 0 ) {
            return new Result(BAD_REQUEST, "查看错误");
        }
        List<Equipment> equipmentList = equipmentService.selectAppointment(userId);

        return new Result(SUCCESS,equipmentList, "查询成功");
    }

    //查看是否预购
    @GetMapping("/isAppointment")
    public Result isAppointment(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();


        if (userId <= 0 || softwareId <= 0 ) {
            return new Result(BAD_REQUEST, false,"查看错误");
        }
        boolean isAppointment = equipmentService.isAppointment(userId, softwareId);

        return new Result(SUCCESS, isAppointment, "查询成功") ;
    }

    //加预约
    @PostMapping("/addAppointment")
    public Result addAppointment(@RequestBody Equipment equipment) {
        Long userId = equipment.getUserId();
        Long softwareId = equipment.getSoftwareId();
        if (userId <= 0 || softwareId <= 0 ) {
            return new Result(BAD_REQUEST, "查看错误");
        }
        int addAppointment = equipmentService.addAppointment(equipment);

        return addAppointment > 0 ?  new Result(SUCCESS,"预约成功") : new Result(INTERNAL_ERROR,"预约失败，请稍后重试");
    }


    //管理员看预约
    @GetMapping("/findAllAppointment")
    public Result findAllAppointment() {
        List<Equipment> equipmentList = equipmentService.selectAllAppointment();
        return equipmentList != null ?  new Result(SUCCESS,equipmentList,"查看成功") : new Result(INTERNAL_ERROR,"查看失败，请稍后重试");
    }





}
