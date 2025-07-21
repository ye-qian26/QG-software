package com.qg.service.impl;

import com.qg.domain.Equipment;
import com.qg.mapper.EquipmentMapper;
import com.qg.service.EquipmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EquipmentServiceImpl implements EquipmentService {

    @Autowired
    EquipmentMapper equipmentMapper;

    @Override
    public int saveEquipment(Equipment equipment) {
        return equipmentMapper.insert(equipment);
    }
}
