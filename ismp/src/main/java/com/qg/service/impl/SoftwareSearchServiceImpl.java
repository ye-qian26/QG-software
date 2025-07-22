package com.qg.service.impl;

import com.qg.domain.Software;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.SoftwareSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class SoftwareSearchServiceImpl implements SoftwareSearchService {

    @Autowired
    private SoftwareMapper softwareMapper;

    //主页轮播图接口
    public List<Software> SearchSoftwareNew(){
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectTop10ByStatusOrderByIdDesc(1);
        return list;
    }

}
