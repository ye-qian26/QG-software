package com.qg.controller;


import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/softwares")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    //软件发布
    @PostMapping("/addSoftware")
    public Result addSoftware(@RequestBody Software software) {
        //System.out.println(software.getAuthorId());
        int sum = softwareService.addSoftware(software);
        if (sum > 0) {
            Result result = new Result(Code.SUCCESS, "软件上传成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "软件上传失败！");
            return result;
        }
    }

    @GetMapping("/selectByStatus")
    public Result CheckSoftwareList(@RequestParam Integer status) {
        List<Software> softwareList =  softwareService.CheckSoftwareList(status);
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    @GetMapping
    public Result getAllSoftwareList() {
        List<Software> softwareList = softwareService.getAllSoftwareList();
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    @PutMapping("/updateSoftware")
    public Result updateSoftware(@RequestBody Software software) {
        Long id = software.getId();
        int sum = 0;
        sum= softwareService.updateSoftware(id);
        if (sum > 0) {
            Result result = new Result(Code.SUCCESS, "信息修改成功！");
            return result;
        }else {
            Result result = new Result(Code.BAD_REQUEST, "信息修改失败！");
            return result;
        }

    }

    @PostMapping("/roleUpdate")
    public Result roleUpdate(@RequestBody Software software) {
        Long id = software.getId();
        int sum = 0;
        sum= softwareService.roleUpdate(id);
        if (sum > 0) {
            Result result = new Result(Code.SUCCESS, "信息修改成功！");
            return result;
        }else {
            Result result = new Result(Code.BAD_REQUEST, "信息修改失败！");
            return result;
        }

    }





}
