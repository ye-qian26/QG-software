package com.qg.service;

import com.qg.domain.Equipment;
import com.qg.domain.Software;

import java.util.List;

public interface EquipmentService {
    int saveEquipment(Equipment equipment);

    List<Software> selectPurchased(Long userId);

    boolean isPurchased(Long userId, Long softwareId);

    List<Software> selectAppointment(Long userId);

    boolean isAppointment(Long userId, Long softwareId);

    int addAppointment(Equipment equipment);


    List<Equipment> selectAllAppointment();

    boolean updateCode(Equipment equipment);


    List<Software> adminGetAllOrderSoftware();

}
