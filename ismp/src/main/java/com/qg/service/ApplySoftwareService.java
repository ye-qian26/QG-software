package com.qg.service;

import com.qg.domain.ApplyDeveloper;
import com.qg.domain.ApplySoftware;

import java.util.List;

public interface ApplySoftwareService {

    /**
     * 按照申请时间查询所有申请
     */
    List<ApplySoftware> selectAllOrderByTime();

    boolean add(ApplySoftware ApplySoftware);

    List<ApplySoftware> selectByUserId(Long userId);

    boolean delete(ApplySoftware ApplySoftware);

    ApplySoftware selectById(Long id);

    boolean updateStatus(ApplySoftware ApplySoftware);
}
