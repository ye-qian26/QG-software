package com.qg.service;

import com.qg.domain.Equipment;

import java.util.List;

public interface EquipmentService {
    int saveEquipment(Equipment equipment);

    List selectPurchased(Long userId);

    boolean isPurchased(Long userId, Long softwareId);

    List<Equipment> selectAppointment(Long userId);

    boolean isAppointment(Long userId, Long softwareId);

    int addAppointment(Equipment equipment);

    List<Equipment> selectAllAppointment();

    boolean updateCode(Equipment equipment);

}
