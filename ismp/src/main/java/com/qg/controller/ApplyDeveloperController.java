package com.qg.controller;

import com.qg.domain.ApplyDeveloper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.ApplyDeveloperService;
import com.qg.utils.FileUploadHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static com.qg.utils.FileUploadHandler.DOCUMENT_DIR;

@RestController
@RequestMapping("/applyDevelopers")
public class ApplyDeveloperController {

    @Autowired
    private ApplyDeveloperService applyDeveloperService;

    /**
     * 查询所有 成为开发商 申请（按时间降序排列）
     * @return
     */
    @GetMapping
    public Result selectAllOrderByTime() {
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectAllOrderByTime();
        Integer code = applyDevelopers != null && !applyDevelopers.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDevelopers != null && !applyDevelopers.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, applyDevelopers, msg);
    }

    /**
     * 添加 成为开发商 申请
     * @param applyDeveloper
     * @return
     */
    @PostMapping
    public Result add(@RequestBody ApplyDeveloper applyDeveloper, @RequestBody MultipartFile file) {
        try {
            // 判断 文件 类型
            if (!FileUploadHandler.isValidDocumentFile(file)) {
                return new Result(Code.BAD_REQUEST, "文档格式错误");
            }
            // 保存文件到服务器上，并获取绝对路径
            String filePath = FileUploadHandler.saveFile(file, DOCUMENT_DIR);
            // 添加绝对路径到 applyDeveloper 中
            applyDeveloper.setMaterial(filePath);

            boolean flag = applyDeveloperService.add(applyDeveloper);
            Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
            String msg = flag ? "" : "添加失败，请稍后重试！";
            return new Result(code, msg);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 删除 成为开发商申请 （逻辑删除）
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

    /**
     * 管理员 修改 申请 处理状态
     * @param applyDeveloper
     * @return
     */
    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody ApplyDeveloper applyDeveloper) {
        boolean flag = applyDeveloperService.updateStatus(applyDeveloper);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 管理员根据 id 修改 申请 处理状态
     * @param id
     * @return
     */
    @PutMapping("/updateStatus/{id}")
    public Result updateStatusById(@PathVariable Long id) {
        if (id == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        boolean flag = applyDeveloperService.updateStatusById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }
}
