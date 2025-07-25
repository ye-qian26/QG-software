package com.qg.controller;

import cn.hutool.core.util.StrUtil;
import com.qg.domain.ApplySoftware;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.ApplySoftwareService;
import com.qg.utils.FileUploadHandler;
import com.qg.utils.JsonParserUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.qg.utils.FileUploadHandler.DOCUMENT_DIR;

@RestController
@RequestMapping("/applySoftwares")
public class ApplySoftwareController {

    @Autowired
    private ApplySoftwareService applySoftwareService;

    /**
     * 查询所有 发布软件 申请 （按时间降序排序）
     * @return
     */
    @GetMapping
    public Result selectAllOrderByTime() {
        List<ApplySoftware> applySoftwares = applySoftwareService.selectAllOrderByTime();
        Integer code = applySoftwares != null && !applySoftwares.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftwares != null && !applySoftwares.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, applySoftwares, msg);
    }

    /**
     * 添加 发布软件 申请
     * @param applySoftwareJson
     * @return
     */
    @PostMapping
    public Result add(@RequestParam("applySoftware") String applySoftwareJson, @RequestBody MultipartFile file) {
        try {
            // 判断 文件类型
            if (!FileUploadHandler.isValidDocumentFile(file)) {
                // 文档 类型错误
                return new Result(Code.BAD_REQUEST, "文档类型错误");
            }
            // 保存文件到服务器上，并获取绝对路径
            String filePath = FileUploadHandler.saveFile(file, DOCUMENT_DIR);
            // 1. 解析 JSON 字符串为 Software 对象
            ApplySoftware applySoftware = JsonParserUtil.fromJson(applySoftwareJson, ApplySoftware.class);

            // 将 绝对路径 保存 到 applySoftware 中
            applySoftware.setMaterial(filePath);

            boolean flag = applySoftwareService.add(applySoftware);
            Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
            String msg = flag ? "" : "添加失败，请稍后重试！";
            return new Result(code, msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除 发布软件 申请 （逻辑删除）
     * @param applySoftware
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestBody ApplySoftware applySoftware) {
        boolean flag = applySoftwareService.delete(applySoftware);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 根据 id 删除 发布软件 申请 （逻辑删除）
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        boolean flag = applySoftwareService.deleteById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 根据 userId 查询 发布软件 申请
     * @param userId
     * @return
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId) {
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        List<ApplySoftware> applySoftwares = applySoftwareService.selectByUserId(userId);
        Integer code = applySoftwares != null && !applySoftwares.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftwares != null && !applySoftwares.isEmpty() ? "" : "未查询到相关数据";
        return new Result(code, applySoftwares, msg);
    }

    /**
     * 根据 id 查询 发布软件申请
     * @param id
     * @return
     */
    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        ApplySoftware applySoftware = applySoftwareService.selectById(id);
        Integer code = applySoftware != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftware != null ? "" : "未查询到相关数据";
        return new Result(code, applySoftware, msg);
    }



//    /**
//     * 管理员 修改 申请 处理状态
//     * @param applySoftware
//     * @return
//     */
//    @PutMapping("/updateStatus")
//    public Result updateStatus(@RequestBody ApplySoftware applySoftware) {
//        boolean flag = applySoftwareService.updateStatus(applySoftware);
//        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
//        String msg = flag ? "" : "更改状态失败，请稍后重试！";
//        return new Result(code, msg);
//    }
//
//    /**
//     * 管理根据 id 修改 处理状态
//     * @param id
//     * @return
//     */
//    @PutMapping("/updateStatus/{id}")
//    public Result updateStatusById(@PathVariable Long id) {
//        if (id == null) {
//            return new Result(Code.BAD_REQUEST, "请求参数错误");
//        }
//        boolean flag = applySoftwareService.updateStatusById(id);
//        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
//        String msg = flag ? "" : "更改状态失败，请稍后重试！";
//        return new Result(code, msg);
//    }
//


    /**
     * 同意
     * 申请发布软件
     *
     * @param applySoftware
     * @return
     */
    @PutMapping("/agreeApplySoftware")
    public Result agreeApplySoftware(@RequestBody ApplySoftware applySoftware) {
        if (applySoftware== null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        if (applySoftwareService.agreeApplySoftware(applySoftware)) {
            return new Result(Code.SUCCESS, "批准成功，已通知用户");
        } else {
            return new Result(Code.NOT_FOUND, "找不到相关申请请求，可能是用户不存在，也可能是已审核");
        }
    }


    /**
     * 驳回
     * 申请发布软件
     *
     * @param applySoftware
     * @return
     */
    @PutMapping("/disagreeApplySoftware")
    public Result disagreeApplySoftware(@RequestBody ApplySoftware applySoftware) {
        if (applySoftware == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        if (StrUtil.isBlank(applySoftware.getReason())) {
            return new Result(Code.BAD_REQUEST, "请输入驳回理由");
        }
        if (applySoftwareService.disagreeApplySoftware(applySoftware)) {
            return new Result(Code.SUCCESS, "驳回成功，已通知用户");
        } else {
            return new Result(Code.NOT_FOUND, "找不到相关申请请求，可能是用户不存在，也可能是已审核");
        }
    }
}
