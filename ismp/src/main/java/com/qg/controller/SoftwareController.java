package com.qg.controller;


import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareService;
import com.qg.utils.FileUploadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import static com.qg.utils.FileUploadHandler.IMAGE_DIR;

@RestController
@RequestMapping("/softwares")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    /**
     * 软件发布
     * @param software
     * @param file
     * @return
     */
    @PostMapping("/addSoftware")
    public Result addSoftware(@RequestBody Software software,@RequestBody MultipartFile file) {
        try {

            System.out.println(file.getOriginalFilename());
            // 判断 文件类型
            if (!FileUploadHandler.isValidDocumentFile(file)) {
                // 文档 类型错误
                return new Result(Code.BAD_REQUEST, "文档类型错误");
            }
            // 保存文件到服务器上，并获取绝对路径
            String filePath = FileUploadHandler.saveFile(file, IMAGE_DIR);
            //保存绝对路径到software的link变量里
            software.setPicture(filePath);
            //System.out.println(software.getAuthorId());
            int sum = softwareService.addSoftware(software);
            if (sum > 0) {
                Result result = new Result(Code.SUCCESS, "软件上传成功！");
                return result;
            } else {
                Result result = new Result(Code.BAD_REQUEST, "软件上传失败！");
                return result;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 管理员查看已审核/未审核状态的软件
     * @param status
     * @return
     */
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

    /**
     * 管理员查看所有软件
     * @return
     */
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

    /**
     * 第三方修改软件为已发布（用户可下载）
     * @param software
     * @return
     */
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

    /**
     * 管理员修改软件状态为已审核
     * @param software
     * @return
     */
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

    /**
     * 第三方逻辑下架软件
     * @param software
     * @return
     */
    @DeleteMapping
    public Result deleteSoftware(@RequestBody Software software) {
        Long id = software.getId();
        int sum = 0;
        sum= softwareService.deleteSoftware(id);
        if (sum > 0) {
            Result result = new Result(Code.SUCCESS, "信息修改成功！");
            return result;
        }else {
            Result result = new Result(Code.BAD_REQUEST, "信息修改失败！");
            return result;
        }
    }

    /**
     * 管理员逻辑下架软件
     * @param software
     * @return
     */
    @DeleteMapping
    public Result roleDelete(@RequestBody Software software) {
        Long id = software.getId();
        int sum = 0;
        sum= softwareService.roleDelete(id);
        if (sum > 0) {
            Result result = new Result(Code.SUCCESS, "信息修改成功！");
            return result;
        }else {
            Result result = new Result(Code.BAD_REQUEST, "信息修改失败！");
            return result;
        }
    }


}
