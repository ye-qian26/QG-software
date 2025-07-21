package com.qg.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.qg.domain.ApplyDeveloper;

import java.util.List;

public interface ApplyDeveloperService {
    /**
     * 按照申请时间查询所有申请
     */
    List<ApplyDeveloper> selectAllOrderByTime();

    boolean add(ApplyDeveloper applyDeveloper);

    List<ApplyDeveloper> selectByUserId(Long userId);

    boolean delete(ApplyDeveloper applyDeveloper);

    ApplyDeveloper selectById(Long id);

    boolean updateStatus(ApplyDeveloper applyDeveloper);
}
