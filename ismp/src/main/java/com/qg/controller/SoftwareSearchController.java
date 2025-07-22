package com.qg.controller;

import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareSearchService;
import com.qg.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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

    //类别的最新十个的接口
    @GetMapping("/SearchTypeNew")
    public Result SearchTypeNew(@RequestParam String type){
        List<Software> softwareList = softwareSearchService.SearchTypeNew(type);
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }


    @GetMapping("/SearchSoftwareType")
    public Result SearchSoftwareType(@RequestParam String type){
        List<Software> softwareList = softwareSearchService.SearchSoftwareType(type);
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    @GetMapping("/SearchSoftware")
    public Result SearchSoftware(@RequestParam Long id){
        Software software = softwareSearchService.SearchSoftware(id);
        if (software != null) {
            Result result = new Result(Code.SUCCESS, software, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    @GetMapping("/SearchSoftwareVersion")
    public Result SearchSoftwareVersion(@RequestParam Long id){
        List<Software> list = softwareSearchService.SearchSoftwareVersion(id);
        if (list.size() > 0) {
            Result result = new Result(Code.SUCCESS, list, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }
}
