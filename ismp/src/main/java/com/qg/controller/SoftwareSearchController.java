package com.qg.controller;

import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareSearchService;
import com.qg.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/softwares")
public class SoftwareSearchController {

    @Autowired
    private SoftwareSearchService softwareSearchService;

    //轮播图接口
    @GetMapping("/SearchSoftwareNew")
    public Result SearchSoftwareNew(){
        List<Software> softwareList = softwareSearchService.SearchSoftwareNew();
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }


}
