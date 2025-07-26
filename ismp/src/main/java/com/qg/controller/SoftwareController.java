package com.qg.controller;


import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.SoftwareService;
import com.qg.utils.FileUploadHandler;
import com.qg.utils.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;


import static com.qg.utils.FileUploadHandler.IMAGE_DIR;
import static com.qg.utils.FileUploadHandler.INSTALL_DIR;

@RestController
@RequestMapping("/softwares")
public class SoftwareController {

    @Autowired
    private SoftwareService softwareService;

    /**
     * 软件发布
     * @param softwareJson
     * @param picture
     * @return
     */
    @PostMapping("/addSoftware")
    public Result addSoftware(@RequestParam("software") String softwareJson, @RequestParam("picture") MultipartFile picture, @RequestParam("file") MultipartFile file) {
        try {
            if (softwareJson == null || picture == null || file == null) {
                return new Result(Code.BAD_REQUEST, "传递的请求参数为空值，请检查");
            }
            System.out.println("addSoftware ===>>>" + softwareJson);
            System.out.println("addSoftware ===>>> 图片 文件名" + picture.getOriginalFilename());
            System.out.println("addSoftware ===>>> 文档 文件名" + file.getOriginalFilename());
            Software software = JsonParserUtil.fromJson(softwareJson, Software.class);
            System.out.println("addSoftware ===>>> json解析后的software： " + software);
            if (software == null) {
                return new Result(Code.BAD_REQUEST, "软件信息解析失败");
            }
            // 判断 文件类型
            if (!FileUploadHandler.isValidImageFile(picture) || !FileUploadHandler.isValidInstallFile(file)) {
                // 文档 类型错误
                return new Result(Code.BAD_REQUEST, "类型错误");
            }
            // 保存文件到服务器上，并获取绝对路径
            String picturePath = FileUploadHandler.saveFile(picture, IMAGE_DIR);
            String linkPath = FileUploadHandler.saveFile(file, INSTALL_DIR);

            //保存绝对路径到software的link变量里
            software.setPicture(picturePath);
            software.setLink(linkPath);
            System.out.println(software);
            //System.out.println(software.getAuthorId());
            Software software1 = softwareService.addSoftware(software);
            if (software1 != null) {
                Result result = new Result(Code.SUCCESS, software1,"软件上传成功！");
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
    @PutMapping("/roleUpdate")
    public Result roleUpdate(@RequestBody Software software) {
        System.out.println("roleUpdate ===>>>" + software);
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
    @DeleteMapping("/deleteSoftware")
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
     * @param id
     * @return
     */
    @DeleteMapping("/roleDelete")
    public Result roleDelete(@RequestParam Long id) {
        System.out.println("roleDelete ===>>>" + id);
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

    /**
     * 第三方修改软件信息
     *
     * @param software
     * @return
     */
    @PutMapping("/changeSoftwareById")
    public Result changeSoftwareById(@RequestBody Software software) {
        System.out.println("changeSoftwareById ===>>>" + software);
        Software software1=softwareService.changeSoftwareById(software);
        if (software1 != null) {
            Result result = new Result(Code.SUCCESS, software1,"信息获取成功！");
            return result;
        }
        else {
            Result result = new Result(Code.BAD_REQUEST,"信息获取失败！");
            return result;
        }
    }
}
