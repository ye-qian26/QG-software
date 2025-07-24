package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Equipment;
import com.qg.domain.Software;
import com.qg.mapper.EquipmentMapper;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.qg.utils.Constants.EQUIPMENT_STATUS_BOUGHT;
import static com.qg.utils.Constants.EQUIPMENT_STATUS_ORDER;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    EquipmentMapper equipmentMapper;
    @Autowired
    SoftwareMapper softwareMapper;

    @Override
    public int saveEquipment(Equipment equipment) {
        equipment.setStatus(EQUIPMENT_STATUS_BOUGHT);
        return equipmentMapper.insert(equipment);
    }

    /**
     * 获取用户的所有已购买软件
     * @param userId
     * @return
     */
    @Override
    public List<Software> selectPurchased(Long userId) {
        return softwareMapper.getAllBuySoftware(userId);
    }

    @Override
    public boolean isPurchased(Long userId, Long softwareId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, userId).eq(Equipment::getSoftwareId, softwareId).eq(Equipment::getStatus, EQUIPMENT_STATUS_BOUGHT);
        Equipment equipment = equipmentMapper.selectOne(queryWrapper);

        return equipment != null;
    }

    /**
     * 获取用户的所有已预约软件
     * @param userId
     * @return
     */
    @Override
    public List<Software> selectAppointment(Long userId) {
        return softwareMapper.getAllOrderSoftware(userId);
    }

    @Override
    public boolean isAppointment(Long userId, Long softwareId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, userId).eq(Equipment::getSoftwareId, softwareId).eq(Equipment::getStatus, EQUIPMENT_STATUS_ORDER);
        Equipment equipment = equipmentMapper.selectOne(queryWrapper);
        return equipment != null;
    }

    @Override
    public int addAppointment(Equipment equipment) {
        equipment.setStatus(EQUIPMENT_STATUS_ORDER);
        return equipmentMapper.insert(equipment);
    }


    /**
     * 管理员查看所有用户的预约软件
     * @return
     */
    public List<Software> adminGetAllOrderSoftware() {
        return softwareMapper.adminGetAllOrderSoftware();
    }

    @Override
    public boolean updateCode(Equipment equipment) {
        return equipmentMapper.updateById(equipment) > 0;
    }

}
