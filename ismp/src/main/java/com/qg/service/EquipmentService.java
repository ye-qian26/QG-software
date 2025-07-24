package com.qg.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.qg.domain.Equipment;
import com.qg.domain.Software;

import java.util.List;

public interface EquipmentService {

    int saveEquipment(Equipment equipment);

    IPage<Software> selectPurchased(Long userId, Integer current, Integer size);

    boolean isPurchased(Long userId, Long softwareId);

    IPage<Software> selectAppointment(Long userId, Integer current, Integer size);

    boolean isAppointment(Long userId, Long softwareId);

    int addAppointment(Equipment equipment);

    IPage<Software> adminGetAllOrderSoftware(Integer current, Integer size);

    boolean updateCode(Equipment equipment);


}
