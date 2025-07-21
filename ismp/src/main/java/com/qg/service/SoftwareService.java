package com.qg.service;

import com.qg.domain.Result;
import com.qg.domain.Software;

import java.util.List;

public interface SoftwareService {
    public int addSoftware(Software software);

    public List<Software> CheckSoftwareList();
}
