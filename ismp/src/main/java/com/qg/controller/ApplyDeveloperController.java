package com.qg.controller;

import cn.hutool.core.util.StrUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.qg.domain.ApplyDeveloper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.domain.Software;
import com.qg.service.ApplyDeveloperService;
import com.qg.utils.FileUploadHandler;
import com.qg.utils.JsonParserUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.qg.utils.FileUploadHandler.DOCUMENT_DIR;

@Slf4j
@RestController
@RequestMapping("/applyDevelopers")
public class ApplyDeveloperController {

    @Autowired
    private ApplyDeveloperService applyDeveloperService;

    /**
     * 查询所有 成为开发商 申请（按时间降序排列）
     *
     * @return
     */
    @GetMapping
    public Result selectAllOrderByTime() {
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectAllOrderByTime();
        System.out.println("applyDevelopers ===>>>" + applyDevelopers);
        Integer code = applyDevelopers != null && !applyDevelopers.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDevelopers != null && !applyDevelopers.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, applyDevelopers, msg);
    }

    /**
     * 添加 成为开发商 申请
     *
     * @param applyDeveloperJson
     * @return
     */
    @PostMapping
    public Result add(@RequestParam("applyDeveloper") String applyDeveloperJson, @RequestParam("file") MultipartFile file) {
        System.out.println("已经进来申请了");
        try {
            if (applyDeveloperJson == null || file == null) {
                System.out.println("传递的请求参数为空值，请检查");
                return new Result(Code.BAD_REQUEST, "传递的请求参数为空值，请检查");
            }

            // 解析 JSON 字符串为 Software 对象
            ApplyDeveloper applyDeveloper = JsonParserUtil.fromJson(applyDeveloperJson, ApplyDeveloper.class);
            if (applyDeveloper == null) {
                System.out.println("json解析出现错误");
                return new Result(Code.BAD_REQUEST, "json解析出现错误");
            }
            // 判断 文件 类型
            if (!FileUploadHandler.isValidDocumentFile(file)) {
                System.out.println("文档错误");
                return new Result(Code.BAD_REQUEST, "文档格式错误");
            }
            // 保存文件到服务器上，并获取绝对路径
            String filePath = FileUploadHandler.saveFile(file, DOCUMENT_DIR);

            // 添加绝对路径到 applyDeveloper 中
            applyDeveloper.setMaterial(filePath);

            boolean flag = applyDeveloperService.add(applyDeveloper);
            Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
            String msg = flag ? "" : "添加失败，请稍后重试！";
            System.out.println("添加 成为开发商 申请结果：" + msg);
            return new Result(code, msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除 成为开发商申请 （逻辑删除）
     *
     * @param applyDeveloper
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestBody ApplyDeveloper applyDeveloper) {
        boolean flag = applyDeveloperService.delete(applyDeveloper);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 根据 id 删除 成为开发商申请 （逻辑删除）
     *
     * @param id
     * @return
     */
    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        boolean flag = applyDeveloperService.deleteById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 根据 userId 查询 成为开发商 申请
     *
     * @param userId
     * @return
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId) {
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectByUserId(userId);
        Integer code = applyDevelopers != null && !applyDevelopers.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDevelopers != null && !applyDevelopers.isEmpty() ? "" : "未查询到相关数据";
        return new Result(code, applyDevelopers, msg);
    }

    /**
     * 根据 id 查询 成为开发商 申请
     *
     * @param id
     * @return
     */
    @GetMapping("/{id}")
    public Result selectById(@PathVariable Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        ApplyDeveloper applyDeveloper = applyDeveloperService.selectById(id);
        Integer code = applyDeveloper != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDeveloper != null ? "" : "未查询到相关数据";
        return new Result(code, applyDeveloper, msg);
    }

//    /**
//     * 管理员 修改 申请 处理状态
//     *
//     * @param applyDeveloper
//     * @return
//     */
//    @PutMapping("/updateStatus")
//    public Result updateStatus(@RequestBody ApplyDeveloper applyDeveloper) {
//        boolean flag = applyDeveloperService.updateStatus(applyDeveloper);
//        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
//        String msg = flag ? "" : "更改状态失败，请稍后重试！";
//        return new Result(code, msg);
//    }
//
//    /**
//     * 管理员根据 id 修改 申请 处理状态
//     *
//     * @param id
//     * @return
//     */
//    @PutMapping("/updateStatus/{id}")
//    public Result updateStatusById(@PathVariable Long id) {
//        if (id == null) {
//            return new Result(Code.BAD_REQUEST, "请求参数错误");
//        }
//        boolean flag = applyDeveloperService.updateStatusById(id);
//        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
//        String msg = flag ? "" : "更改状态失败，请稍后重试！";
//        return new Result(code, msg);
//    }


    /**
     * 同意
     * 申请成为开发者
     *
     * @param applyDeveloper
     * @return
     */
    @PutMapping("/agreeApplyDeveloper")
    public Result agreeApplyDeveloper(@RequestBody ApplyDeveloper applyDeveloper) {
        System.out.println("agreeApplyDeveloper ===>>>" + applyDeveloper);
        if (applyDeveloper == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        if (applyDeveloperService.agreeApplyDeveloper(applyDeveloper)) {
            return new Result(Code.SUCCESS, "批准成功，已通知用户");
        } else {
            return new Result(Code.NOT_FOUND, "找不到相关申请请求，可能是用户不存在，也可能是已审核");
        }
    }


    /**
     * 驳回
     * 申请成为开发者
     *
     * @param applyDeveloper
     * @return
     */
    @PutMapping("/disagreeApplyDeveloper")
    public Result disagreeApplyDeveloper(@RequestBody ApplyDeveloper applyDeveloper) {
        System.out.println("disagreeApplyDeveloper ===>>>" + applyDeveloper);
        if (applyDeveloper == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        if (StrUtil.isBlank(applyDeveloper.getReason())) {
            return new Result(Code.BAD_REQUEST, "请输入驳回理由");
        }
        if (applyDeveloperService.disagreeApplyDeveloper(applyDeveloper)) {
            return new Result(Code.SUCCESS, "驳回成功，已通知用户");
        } else {
            return new Result(Code.NOT_FOUND, "找不到相关申请请求，可能是用户不存在，也可能是已审核");
        }
    }
}
