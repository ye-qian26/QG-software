package com.qg.service;

import com.qg.domain.ApplyDeveloper;
import com.qg.domain.ApplySoftware;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class ApplySoftwareServiceTest {
    @Autowired
    private ApplySoftwareService applySoftwareService;

    @Test
    public void getOrderByTime(){
        List<ApplySoftware> applySoftware = applySoftwareService.selectAllOrderByTime();
        System.out.println(applySoftware);
    }
}
