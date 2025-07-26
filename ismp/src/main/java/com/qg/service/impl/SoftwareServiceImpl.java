package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Equipment;
import com.qg.domain.Software;
import com.qg.mapper.EquipmentMapper;
import com.qg.mapper.MessageMapper;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.MessageService;
import com.qg.service.SoftwareService;
import com.qg.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.qg.utils.Constants.SOFTWARE_STATUS_ORDER;
import static com.qg.utils.Constants.SOFTWARE_STATUS_SALE;

@Service
public class SoftwareServiceImpl implements SoftwareService {
    @Autowired
    private SoftwareMapper softwareMapper;
    @Autowired
    private EquipmentMapper equipmentMapper;
    @Autowired
    private MessageService messageService;

    //审核前需要先上传app信息
    public Software addSoftware(Software software) {
        LambdaQueryWrapper<Software> qw = new LambdaQueryWrapper<>();
        qw.eq(Software::getVersion, software.getVersion());
        qw.eq(Software::getName, software.getName());
        Software software1 = softwareMapper.selectOne(qw);
        if (software1 == null) {
            return softwareMapper.insert(software) > 0 ? software : null;
        }
        return software;
    }

    //管理员待审核/已审核的app信息获取
    public List<Software> CheckSoftwareList(Integer status) {
        List<Software> list = new ArrayList<Software>();
        QueryWrapper<Software> qw = new QueryWrapper<>();
        qw.lambda().eq(Software::getStatus, status);
        list = softwareMapper.selectList(qw);
        //qw.eq("status",i);
        System.out.println(list);
        return list;
    }


    //管理员查看所有的app信息
    public List<Software> getAllSoftwareList() {
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectList(null);
        System.out.println(list);
        return list;
    }

    /**
     * 软件状态从：已预约===>上市，可购买
     *
     * @param id
     * @return
     */
    public int updateSoftware(Long id) {
        // 从equipment表中查出：
        // 该软件id对应的equipment（这些的status都为0）
        QueryWrapper<Equipment> qw = new QueryWrapper<>();
        qw.lambda().eq(Equipment::getSoftwareId, id);
        // 执行删除操作
        equipmentMapper.delete(qw);

        // 给所有预约该产品、关注该产品开发商的用户发通知消息
        messageService.orderSoftwareLaunch(id);

        return softwareMapper.updateStatus(id, SOFTWARE_STATUS_SALE);
    }

    //管理员修改状态
    public int roleUpdate(Long id) {
        int sum = 0;
        sum = softwareMapper.updateStatus(id, SOFTWARE_STATUS_ORDER);
        return sum;
    }

    //第三方逻辑删除
    public int deleteSoftware(Long id) {
        int sum = 0;
        sum = softwareMapper.deleteById(id);
        return sum;
    }

    //管理员逻辑删除
    public int roleDelete(Long id) {
        int sum = 0;
        sum = softwareMapper.deleteById(id);
        return sum;
    }

    //第三方修改software信息
    public Software changeSoftwareById(Software software){
        int sum = 0;
        sum=softwareMapper.updateById(software);
              Long id=software.getId();
        if(sum>0){
            Software softwareNew=softwareMapper.selectById(id);
            return softwareNew;
        }
        else return null;
    }


    /**
     * 获取用户状态
     * * ====>设备状态：0：已预约；1：已购买
     * * ====>假设1:可以预约
     * * =======>可以预约，但是未预约，返回：1null，最后决定返回：1
     * * =======>可以预约，并且已预约，返回：10，最后决定返回：2
     * *
     * * ====>假设2:可以购买
     * * =======>可以购买，但是未购买，返回：2null，最后决定返回：3
     * * =======>可以购买，并且已购买，返回：21，最后决定返回：4
     *
     * @param userId
     * @param softwareId
     * @return
     */
    @Override
    public Integer checkSoftwareStatus(Long userId, Long softwareId) {

        // 从software表中获取对应的software对象
        LambdaQueryWrapper<Software> softwareQueryWrapper = new LambdaQueryWrapper<>();
        softwareQueryWrapper.eq(Software::getId, softwareId);
        Software software = softwareMapper.selectOne(softwareQueryWrapper);
        if (Objects.isNull(software)) {
            System.out.println("Objects.isNull(software)");
            return null;
        }
        Integer status = software.getStatus();
        System.out.println("software.getStatus() = " + status);


        // 从equipment表中获取对应的Equipment
        LambdaQueryWrapper<Equipment> equipmentLambdaQueryWrapper = new LambdaQueryWrapper<>();
        equipmentLambdaQueryWrapper
                .eq(Equipment::getSoftwareId, softwareId)
                .eq(Equipment::getUserId, userId);
        Equipment equipment = equipmentMapper.selectOne(equipmentLambdaQueryWrapper);
        System.out.println("equipment = " + equipment);

        // 1x，我们不希望查出来是11
        if (Objects.equals(status, SOFTWARE_STATUS_ORDER)) {
            if (equipment == null) {
                System.out.println("1");
                return 1;
            }
            if (Objects.equals(equipment.getStatus(), Constants.EQUIPMENT_STATUS_ORDER)) {
                System.out.println("2");
                return 2;
            }
        }
        // 2x
        if (Objects.equals(status, SOFTWARE_STATUS_SALE)) {
            if (equipment == null) {
                System.out.println("3");
                return 3;
            }
            if (Objects.equals(equipment.getStatus(), Constants.EQUIPMENT_STATUS_BOUGHT)) {
                System.out.println("4");
                return 4;
            }
        }
        System.out.println("null");
        return null;
    }
}
