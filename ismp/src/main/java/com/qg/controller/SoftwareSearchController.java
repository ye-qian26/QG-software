package com.qg.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareSearchService;
import com.qg.service.SoftwareService;
import com.qg.vo.SoftwareVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
        System.out.println(type + "<==type");
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
        System.out.println(id + "<==controller");
        List<Software> list = softwareSearchService.SearchSoftwareVersion(id);
        System.out.println(list);
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
        System.out.println(authorId + "<==controller");
        List<Software> list = softwareSearchService.selectLastRecordsPerName(authorId);
        System.out.println(list);
        if (list.size() > 0) {
            Result result = new Result(Code.SUCCESS, list, "获取信息成功！");
            return result;
        }
        else {
            Result result = new Result(Code.BAD_REQUEST, "获取信息失败！");
            return result;
        }
    }


    /**
     *
     * @param developerId
     * @return
     */
    @GetMapping("/getSoftwareByDeveloperId/{developerId}")
    public Result getSoftwareByDeveloperId(@PathVariable Long developerId){
        System.out.println(developerId + "<==controller");
        List<Software> list = softwareSearchService.getSoftwareByDeveloperId(developerId);
        if (!list.isEmpty()) {
            return new Result(Code.SUCCESS, list, "获取该开发商的所有产品成功！");
        }
        else {
            return new Result(Code.BAD_REQUEST, "获取该开发商的所有产品失败！");
        }
    }

    /**
     * 管理 审核时 查看 软件详情
     */
    @GetMapping("/getSoftwareWithMaterial")
    public Result getSoftwareWithMaterial(@RequestParam("id") Long id, @RequestParam("authorId") Long userId) {
        System.out.println("id==> " + id + " <==controller");
        System.out.println("userId==> " + userId + "<==controller");
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数出错");
        }
        SoftwareVO softwareVO = softwareSearchService.getSoftwareWithMaterial(id, userId);
        Integer code = softwareVO != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = softwareVO != null ? "" : "未查询到相关信息";
        return new Result(code, softwareVO, msg);
    }
}
