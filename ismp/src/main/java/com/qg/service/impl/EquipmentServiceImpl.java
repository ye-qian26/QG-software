package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Equipment;
import com.qg.mapper.EquipmentMapper;
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

    @Override
    public int saveEquipment(Equipment equipment) {
        equipment.setStatus(EQUIPMENT_STATUS_BOUGHT);
        return equipmentMapper.insert(equipment);
    }

    @Override
    public List selectPurchased(Long userId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, userId).eq(Equipment::getStatus, EQUIPMENT_STATUS_BOUGHT);
        return equipmentMapper.selectList(queryWrapper);
    }

    @Override
    public boolean isPurchased(Long userId, Long softwareId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, userId).eq(Equipment::getSoftwareId, softwareId).eq(Equipment::getStatus, EQUIPMENT_STATUS_BOUGHT);
        Equipment equipment = equipmentMapper.selectOne(queryWrapper);

        return equipment != null;
    }

    @Override
    public List<Equipment> selectAppointment(Long userId) {
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, userId).eq(Equipment::getStatus, EQUIPMENT_STATUS_ORDER);
        List<Equipment> equipments = equipmentMapper.selectList(queryWrapper);

        return equipments;
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
}
