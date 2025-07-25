package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.ApplySoftware;
import com.qg.mapper.ApplySoftwareMapper;
import com.qg.service.ApplySoftwareService;
import com.qg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.qg.utils.Constants.*;


@Service
public class ApplySoftwareServiceImpl implements ApplySoftwareService {
    @Autowired
    private ApplySoftwareMapper applySoftwareMapper;
    @Autowired
    private MessageService messageService;

    @Override
    public List<ApplySoftware> selectAllOrderByTime() {
        // 创建 Lambda 查询包装器
        LambdaQueryWrapper<ApplySoftware> wrapper = new LambdaQueryWrapper<>();
        // 按创建时间降序排序（DESC）
        wrapper.orderByDesc(ApplySoftware::getApplyTime)
                .eq(ApplySoftware::getIsDeleted, IS_NOT_DELETED);
        // 执行查询
        return applySoftwareMapper.selectList(wrapper);
    }

    @Override
    public boolean add(ApplySoftware ApplySoftware) {
        return applySoftwareMapper.insert(ApplySoftware) > 0;
    }

    @Override
    public List<ApplySoftware> selectByUserId(Long userId) {
        // 创建 Lambda 查询包装器
        LambdaQueryWrapper<ApplySoftware> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplySoftware::getUserId, userId);
        return applySoftwareMapper.selectList(wrapper);
    }

    @Override
    public boolean delete(ApplySoftware applySoftware) {
        return applySoftwareMapper.deleteById(applySoftware.getId()) > 0;
    }

    @Override
    public ApplySoftware selectById(Long id) {
        return applySoftwareMapper.selectById(id);
    }

//    @Override
//    public boolean updateStatus(ApplySoftware applySoftware) {
//        if (applySoftware.getStatus() == 0) {
//            applySoftware.setStatus(IS_HANDLED);
//        } else {
//            applySoftware.setStatus(IS_NOT_HANDLED);
//        }
//        return applySoftwareMapper.updateById(applySoftware) > 0;
//    }

    @Override
    public boolean deleteById(Long id) {
        return applySoftwareMapper.deleteById(id) > 0;
    }



    @Override
    public boolean updateStatusById(Long id) {
        ApplySoftware applySoftware = applySoftwareMapper.selectById(id);
        if (applySoftware.getStatus() == 0) {
            applySoftware.setStatus(IS_HANDLED);
        } else {
            return false;
        }
        return applySoftwareMapper.updateById(applySoftware) > 0;
    }



    /**
     * 同意
     * 申请发布软件
     * @param applySoftware
     * @return
     */
    @Override
    public boolean agreeApplySoftware(ApplySoftware applySoftware) {
        return this.updateStatusById(applySoftware.getId())
                && messageService.applySoftwareSuccess(applySoftware);
    }

    /**
     * 驳回
     * 申请发布软件
     * @param applySoftware
     * @return
     */
    @Override
    public boolean disagreeApplySoftware(ApplySoftware applySoftware) {
        return this.updateStatusById(applySoftware.getId())
                && messageService.applySoftwareFailure(applySoftware);
    }
}
