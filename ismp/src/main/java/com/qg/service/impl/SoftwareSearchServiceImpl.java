package com.qg.service.impl;

import com.qg.domain.Software;
import com.qg.mapper.SoftwareMapper;
import com.qg.service.SoftwareSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.qg.utils.Constants.SOFTWARE_STATUS_ORDER;
import static com.qg.utils.Constants.SOFTWARE_STATUS_UNREVIEWED;

@Service
public class SoftwareSearchServiceImpl implements SoftwareSearchService {

    @Autowired
    private SoftwareMapper softwareMapper;

    //主页轮播图接口
    public List<Software> SearchSoftwareNew() {
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectTop10ByStatusOrderByIdDesc(SOFTWARE_STATUS_UNREVIEWED);
        return list;
    }

    //类别的最新10个接口
    public List<Software> SearchTypeNew(String type) {
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectTop10ByStatusAndTypeOrderByIdDesc(SOFTWARE_STATUS_UNREVIEWED, type);
        return list;
    }


    //类别的所有软件
    public List<Software> SearchSoftwareType(String type) {
        List<Software> list = new ArrayList<Software>();
        list = softwareMapper.selectByStatusAndTypeOrderByIdDesc(SOFTWARE_STATUS_UNREVIEWED, type);
        return list;
    }

    //软件详情页
    public Software SearchSoftware(Long id) {
        Software software = softwareMapper.selectById(id);
        return software;
    }

    //该软件不同版本的查看
    public List<Software> SearchSoftwareVersion(Long id) {
        Software software = softwareMapper.selectById(id);
        String name=software.getName();
        List<Software> list=new ArrayList<>();
        list=softwareMapper.selectSoftwareVersion(name);
        return list;
    }

}
