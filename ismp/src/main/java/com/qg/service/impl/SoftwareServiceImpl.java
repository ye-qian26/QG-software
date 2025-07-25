package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.domain.Equipment;
import com.qg.domain.Software;
import com.qg.mapper.EquipmentMapper;
import com.qg.mapper.MessageMapper;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.MessageService;
import com.qg.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

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

        int sum = 0;
        sum = softwareMapper.insert(software);
        if (sum > 0) {
            return software;
        } else {
            return null;
        }

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


    /**
     * 返回用户与软件之间的关系
     * @param userId
     * @param softwareId
     * @return
     */
    @Override
    public Integer checkSoftwareStatus(Long userId, Long softwareId) {
        return softwareMapper.checkSoftwareStatus(userId, softwareId);
    }
}
