package com.qg.service;

import com.qg.domain.ApplyDeveloper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@SpringBootTest
public class ApplyDeveloperServiceTests {
    @Autowired
    private ApplyDeveloperService applyDeveloperService;

    @Test
    public void getOrderByTime(){
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectAllOrderByTime();
        System.out.println(applyDevelopers);
    }

    @Test
    public void add(){
        //3.获取当前时间
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //定义时间格式
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        //格式化时间
        String formattedDateTime = now.format(formatter);
        ApplyDeveloper applyDeveloper = new ApplyDeveloper();
        applyDeveloper.setUserId(1L);
        applyDeveloper.setApplyTime(formattedDateTime);
        applyDeveloper.setReason("");
        applyDeveloper.setMaterial("");
        applyDeveloper.setStatus(0);
        applyDeveloperService.add(applyDeveloper);
    }

    @Test
    public void delete(){
        ApplyDeveloper applyDeveloper = applyDeveloperService.selectById(2L);
        System.out.println(applyDeveloper);
        boolean flag = applyDeveloperService.delete(applyDeveloper);
        System.out.println(flag);
    }

    @Test
    public void updateStatus(){
        ApplyDeveloper applyDeveloper = applyDeveloperService.selectById(2L);
        System.out.println(applyDeveloper);
        applyDeveloperService.updateStatus(applyDeveloper);
        System.out.println(applyDeveloper);
    }
}
