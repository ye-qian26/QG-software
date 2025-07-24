package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.qg.utils.Constants.SOFTWARE_STATUS_ORDER;
import static com.qg.utils.Constants.SOFTWARE_STATUS_SALE;

@Service
public class SoftwareServceImpl implements SoftwareService {
    @Autowired
    private SoftwareMapper softwareMapper;

    //审核前需要先上传app信息
    public Software addSoftware(Software software) {

        int sum = 0;
        sum=softwareMapper.insert(software);
        if(sum>0){
            return software;
        }
        else{
            return null;
        }

    }

    //管理员待审核/已审核的app信息获取
    public List<Software> CheckSoftwareList(Integer status){

        List<Software> list = new ArrayList<Software>();
        QueryWrapper<Software> qw = new QueryWrapper<>();
        qw.lambda().eq(Software::getStatus,status);
        list = softwareMapper.selectList(qw);
        //qw.eq("status",i);
        System.out.println(list);
        return list;

    }


    //管理员查看所有的app信息
    public List<Software> getAllSoftwareList(){
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectList(null);
        System.out.println(list);
        return list;
    }

    //第三方进行修改状态
    public int updateSoftware(Long id){
        int sum = 0;
        sum=softwareMapper.updateStatus(id,SOFTWARE_STATUS_SALE);
        return sum;
    }

    //管理员修改状态
    public int roleUpdate(Long id){
        int sum = 0;
        sum=softwareMapper.updateStatus(id,SOFTWARE_STATUS_ORDER);
        return sum;
    }

    //第三方逻辑删除
    public int deleteSoftware(Long id){
        int sum = 0;
        sum=softwareMapper.deleteById(id);
        return sum;
    }

    //管理员逻辑删除
    public int roleDelete(Long id){
        int sum = 0;
        sum=softwareMapper.deleteById(id);
        return sum;
    }


}
