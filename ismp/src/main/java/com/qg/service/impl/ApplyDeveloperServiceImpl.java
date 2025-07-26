package com.qg.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.ApplyDeveloper;
import com.qg.mapper.ApplyDeveloperMapper;
import com.qg.service.ApplyDeveloperService;
import com.qg.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.qg.utils.Constants.*;

//import static com.qg.util.Constants.*;

@Service
public class ApplyDeveloperServiceImpl implements ApplyDeveloperService {
    @Autowired
    private ApplyDeveloperMapper applyDeveloperMapper;
    @Autowired
    private MessageService messageService;

    @Override
    public List<ApplyDeveloper> selectAllOrderByTime() {
        // 创建 Lambda 查询包装器
        LambdaQueryWrapper<ApplyDeveloper> wrapper = new LambdaQueryWrapper<>();
        // 按创建时间降序排序（DESC）
        wrapper.orderByDesc(ApplyDeveloper::getApplyTime)
                .eq(ApplyDeveloper::getIsDeleted, IS_NOT_DELETED);
        // 执行查询
        return applyDeveloperMapper.selectList(wrapper);
    }

    @Override
    public boolean add(ApplyDeveloper applyDeveloper) {
        return applyDeveloperMapper.insert(applyDeveloper) > 0;
    }

    @Override
    public List<ApplyDeveloper> selectByUserId(Long userId) {
        // 创建 Lambda 查询包装器
        LambdaQueryWrapper<ApplyDeveloper> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ApplyDeveloper::getUserId, userId);
        return applyDeveloperMapper.selectList(wrapper);
    }

    @Override
    public boolean delete(ApplyDeveloper applyDeveloper) {
        return applyDeveloperMapper.deleteById(applyDeveloper.getId()) > 0;
    }

    @Override
    public ApplyDeveloper selectById(Long id) {
        return applyDeveloperMapper.selectById(id);
    }

//    @Override
//    public boolean updateStatus(ApplyDeveloper applyDeveloper) {
//        if (applyDeveloper.getStatus() == 0) {
//            applyDeveloper.setStatus(IS_HANDLED);
//        } else {
//            return false;
//        }
//        return applyDeveloperMapper.updateById(applyDeveloper) > 0;
//    }

    @Override
    public boolean deleteById(Long id) {
        return applyDeveloperMapper.deleteById(id) > 0;
    }

    @Override
    public boolean updateStatusById(Long id) {
        ApplyDeveloper applyDeveloper = applyDeveloperMapper.selectById(id);
        if (applyDeveloper.getStatus() == 0) {
            applyDeveloper.setStatus(IS_HANDLED);
        } else {
            return false;
        }
        return applyDeveloperMapper.updateById(applyDeveloper) > 0;
    }


    /**
     * 同意
     * 申请成为开发者
     * @param applyDeveloper
     * @return
     */
    @Override
    public boolean agreeApplyDeveloper(ApplyDeveloper applyDeveloper) {

        return this.updateStatusById(applyDeveloper.getId())
                && messageService.applyDeveloperSuccess(applyDeveloper.getUserId())
                && applyDeveloperMapper.deleteById(applyDeveloper.getId()) > 0;
    }

    /**
     * 驳回
     * 申请成为开发者
     * @param applyDeveloper
     * @return
     */
    @Override
    public boolean disagreeApplyDeveloper(ApplyDeveloper applyDeveloper) {
        return this.updateStatusById(applyDeveloper.getId())
                && messageService.applyDeveloperFailure(
                        applyDeveloper.getUserId(), applyDeveloper.getReason())
                && applyDeveloperMapper.deleteById(applyDeveloper.getId()) > 0;
    }
}
