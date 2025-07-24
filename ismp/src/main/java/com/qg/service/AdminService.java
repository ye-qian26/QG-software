package com.qg.service;

import com.qg.vo.AdminManageUserVO;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
public interface AdminService {
    List<AdminManageUserVO> getAllUser();

    List<AdminManageUserVO> getUserByName(String name);
}
