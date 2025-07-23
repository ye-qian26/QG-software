package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.Software;

import java.util.List;

public interface SoftwareService {
    public int addSoftware(Software software);

    public List<Software> CheckSoftwareList(Integer status);

    public List<Software> getAllSoftwareList();

    public int updateSoftware(Long id);

    public int roleUpdate(Long id);

    public int deleteSoftware(Long id);

    public int roleDelete(Long id);
}
