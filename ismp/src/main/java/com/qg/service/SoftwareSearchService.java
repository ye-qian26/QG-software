package com.qg.service;

import com.qg.domain.Software;

import java.util.List;

public interface SoftwareSearchService {
    //主页轮播图接口
    public List<Software> SearchSoftwareNew();


    //类别的最新10个接口
    public List<Software> SearchTypeNew(String type);

    //类别的所有软件
    public List<Software> SearchSoftwareType(String type);

    //软件详情页
    public Software SearchSoftware(Long id);

    //不同版本的展示
    public List<Software> SearchSoftwareVersion(Long id);

    List<Software> getSoftwareByFuzzyName(String fuzzyName);

    public List<Software> selectLastRecordsPerName(Long id);

    List<Software> getSoftwareByDeveloperId(Long developerId);


}
