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

    /**
     * 轮播图接口
     * @return
     */
    @GetMapping("/SearchSoftwareNew")
    public Result SearchSoftwareNew() {
        List<Software> softwareList = softwareSearchService.SearchSoftwareNew();
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    /**
     * 类别的最新十个的接口
     * @param type
     * @return
     */
    @GetMapping("/SearchTypeNew")
    public Result SearchTypeNew(@RequestParam String type) {
        List<Software> softwareList = softwareSearchService.SearchTypeNew(type);
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    /**
     * 查看该类别的所有软件
     * @param type
     * @return
     */
    @GetMapping("/SearchSoftwareType")
    public Result SearchSoftwareType(@RequestParam String type) {
        List<Software> softwareList = softwareSearchService.SearchSoftwareType(type);
        if (softwareList.size() > 0) {
            Result result = new Result(Code.SUCCESS, softwareList, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    /**
     * 用户查看软件详情
     * @param id
     * @return
     */
    @GetMapping("/SearchSoftware")
    public Result SearchSoftware(@RequestParam Long id) {
        Software software = softwareSearchService.SearchSoftware(id);
        if (software != null) {
            Result result = new Result(Code.SUCCESS, software, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    /**
     * 查看该软件的多个版本
     * @param id
     * @return
     */
    @GetMapping("/SearchSoftwareVersion")
    public Result SearchSoftwareVersion(@RequestParam Long id) {
        List<Software> list = softwareSearchService.SearchSoftwareVersion(id);
        if (list.size() > 0) {
            Result result = new Result(Code.SUCCESS, list, "获取信息成功！");
            return result;
        } else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }

    /**
     * 软件的模糊查询
     * @param name
     * @return
     */
    @GetMapping("/getSoftwareByFuzzyName")
    public Result getSoftwareByFuzzyName(@RequestParam String name) {
        if (name == null || name.trim().isEmpty()) {
            return new Result(Code.BAD_REQUEST, "请输入文本");
        }

        // 根据软件名称进行模糊匹配
        List<Software> softwareList = softwareSearchService.getSoftwareByFuzzyName(name.trim());

        if (!softwareList.isEmpty()) {
            return new Result(Code.SUCCESS, softwareList, "查询成功！");
        } else {
            return new Result(Code.NOT_FOUND, "查不到相关软件");
        }
    }


    /**
     * 查看各个软件的最新版本
     * @param authorId
     * @return
     */
    @GetMapping("/selectLastRecordsPerName")
    public Result selectLastRecordsPerName(@RequestParam Long authorId){
        List<Software> list = softwareSearchService.selectLastRecordsPerName(authorId);
        if (list.size() > 0) {
            Result result = new Result(Code.SUCCESS, list, "获取信息成功！");
            return result;
        }
        else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }
}
