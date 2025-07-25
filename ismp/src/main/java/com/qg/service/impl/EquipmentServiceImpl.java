package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.qg.domain.Equipment;
import com.qg.domain.Software;
import com.qg.mapper.EquipmentMapper;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.EquipmentService;
import com.qg.utils.NetWorkCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.net.SocketException;
import java.net.UnknownHostException;


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
        Long softwareId = equipment.getSoftwareId();
        Software software = softwareMapper.selectById(softwareId);
        equipment.setName(software.getName());
        return equipmentMapper.insert(equipment);
    }

    /**
     * 获取用户的所有已购买软件
     *
     * @param userId
     * @return
     */
    @Override
    public IPage<Software> selectPurchased(Long userId, Integer current, Integer size) {
        System.out.println("==>userId" + userId + "==>current" + current + "==>size" + size);
        Page<Software> page = new Page<>(current, size);
        return softwareMapper.selectPurchased(page, userId);
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
     *
     * @param userId
     * @return
     */
    @Override
    public IPage<Software> selectAppointment(Long userId, Integer current, Integer size) {
        Page<Software> page = new Page<>(current, size);
        return softwareMapper.getAllOrderSoftware(page, userId);
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
     *
     * @return
     */
    @Override
    public IPage<Software> adminGetAllOrderSoftware(Integer current, Integer size) {
        Page<Software> page = new Page<>(current, size);
        return softwareMapper.adminGetAllOrderSoftware(page);
    }

    @Override
    public boolean addNetWorkCode(Equipment equipment) throws SocketException, UnknownHostException {
        String netWorkCode = NetWorkCode.getNetWorkCode();
        System.out.println("===>addNetWorkCode：" + netWorkCode);
        LambdaQueryWrapper<Equipment> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Equipment::getUserId, equipment.getUserId())
                .eq(Equipment::getSoftwareId, equipment.getSoftwareId())
                .eq(Equipment::getName, equipment.getName());


        Equipment one = equipmentMapper.selectOne(queryWrapper);

        if (one == null) {
            System.out.println("====>预约记录不存在");
            return false;
        }

        // 检查网络码是否已存在
        if (netWorkCode.equals(one.getCode1()) ||
                netWorkCode.equals(one.getCode2()) ||
                netWorkCode.equals(one.getCode3())) {
            System.out.println("绑定失败：网络码已存在");
            return false;
        }

        // 检查绑定数量是否已达上限
        if (one.getCode1() != null && one.getCode2() != null && one.getCode3() != null) {
            System.out.println("绑定失败：设备已绑定3个网络码");
            return false;
        }

        if (one.getCode1() == null) {
            one.setCode1(netWorkCode);
        } else if (one.getCode2() == null) {
            one.setCode2(netWorkCode);
        } else if (one.getCode3() == null) {
            one.setCode3(netWorkCode);
        }

        return equipmentMapper.updateById(one) > 0;
    }

    @Override
    public boolean updateCode(Equipment equipment) {
        return equipmentMapper.updateById(equipment) > 0;
    }

    @Override
    public int GetUserStatus(Long userId) {
        int status = equipmentMapper.selectById(userId).getStatus();
        System.out.println("返回的状态码：" + status);
        return status;
    }

}
