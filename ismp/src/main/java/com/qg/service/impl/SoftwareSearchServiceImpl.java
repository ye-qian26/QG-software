package com.qg.service.impl;

import com.qg.domain.Software;
import com.qg.mapper.SoftwareMapper;
import com.qg.repository.SoftwareRedisRepository;
import com.qg.service.SoftwareSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static com.qg.utils.Constants.SOFTWARE_STATUS_UNREVIEWED;

@Service
public class SoftwareSearchServiceImpl implements SoftwareSearchService {

    @Autowired
    private SoftwareMapper softwareMapper;
    @Autowired
    private SoftwareRedisRepository softwareRedisRepository;

    //主页轮播图接口
    public List<Software> SearchSoftwareNew() {
        System.out.println("package com.qg.service.impl.SearchSoftwareNew");
        // 先尝试从Redis获取缓存数据
        List<Software> cachedList = softwareRedisRepository.getPublishedPicture();
        if (cachedList != null && !cachedList.isEmpty()) {
            return cachedList;
        }
        System.out.println("缓存不存在");

        // 缓存不存在，从数据库查询
        List<Software> dbList = softwareMapper
                .selectTop10LatestPerNameExcludingStatus(SOFTWARE_STATUS_UNREVIEWED);
        System.out.println("数据库查询成功" + dbList);

        // 将查询结果存入Redis缓存
        if (dbList != null && !dbList.isEmpty()) {
            softwareRedisRepository.cachePublishedPicture(dbList);
            System.out.println("存入缓存成功");
        }
        return dbList;
    }

    //类别的最新10个接口
    public List<Software> SearchTypeNew(String type) {
        System.out.println("package com.qg.service.impl.SearchTypeNew" + type);
        // 先尝试从Redis获取缓存数据
        List<Software> cachedList = softwareRedisRepository.getCachedTypeNew(type);
        if (cachedList != null && !cachedList.isEmpty()) {
            return cachedList;
        }
        System.out.println("缓存不存在");

        // 缓存不存在，从数据库查询
        List<Software> dbList = softwareMapper
                .selectTop8LatestPerName(SOFTWARE_STATUS_UNREVIEWED, type);
        System.out.println("数据库查询成功" + dbList);

        // 将查询结果存入Redis缓存
        if (dbList != null && !dbList.isEmpty()) {
            softwareRedisRepository.cacheTypeNew(type, dbList);
            System.out.println("存入缓存成功");
        }
        return dbList;
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

    //同名软件只返回最新的版本
    public List<Software> selectLastRecordsPerName(Long authorId) {
        List<Software> list = softwareMapper.selectLastRecordsPerName(authorId);
        return list;
    }


    /**
     * 根据软件名称模糊匹配
     * @param fuzzyName
     * @return
     */
    @Override
    public List<Software> getSoftwareByFuzzyName(String fuzzyName) {
        return softwareMapper.getSoftwareByFuzzyName(fuzzyName);
    }

}
