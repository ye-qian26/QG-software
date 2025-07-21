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

@Service
public class SoftwareServceImpl implements SoftwareService {
    @Autowired
    private SoftwareMapper softwareMapper;

    //审核前需要先上传app信息
    public int addSoftware(Software software) {

        int sum = 0;
        sum=softwareMapper.insert(software);
        return sum;
    }

    //管理员审核的app信息获取

    public List<Software> CheckSoftwareList(){
        List<Software> list = new ArrayList<Software>();
        QueryWrapper<Software> qw = new QueryWrapper<>();
        qw.lambda().eq(Software::getStatus,0);
        list = softwareMapper.selectList(qw);
        //qw.eq("status",i);
        System.out.println(list);
        return list;

    }



    //用户查看软件信息

}
