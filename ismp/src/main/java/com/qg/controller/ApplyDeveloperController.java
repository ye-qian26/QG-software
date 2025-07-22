package com.qg.controller;

import com.qg.domain.ApplyDeveloper;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.ApplyDeveloperService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applyDevelopers")
public class ApplyDeveloperController {

    @Autowired
    private ApplyDeveloperService applyDeveloperService;

    @GetMapping
    public Result selectAllOrderByTime() {
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectAllOrderByTime();
        Integer code = applyDevelopers != null && !applyDevelopers.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDevelopers != null && !applyDevelopers.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, applyDevelopers, msg);
    }

    @PostMapping
    public Result add(@RequestBody ApplyDeveloper applyDeveloper) {
        boolean flag = applyDeveloperService.add(applyDeveloper);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "添加失败，请稍后重试！";
        return new Result(code, msg);
    }

    @DeleteMapping
    public Result delete(@RequestBody ApplyDeveloper applyDeveloper) {
        boolean flag = applyDeveloperService.delete(applyDeveloper);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        boolean flag = applyDeveloperService.deleteById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId) {
        List<ApplyDeveloper> applyDevelopers = applyDeveloperService.selectByUserId(userId);
        Integer code = applyDevelopers != null && !applyDevelopers.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDevelopers != null && !applyDevelopers.isEmpty() ? "" : "未查询到相关数据";
        return new Result(code, applyDevelopers, msg);
    }

    @GetMapping("/{id}")
    public Result selectById(@PathVariable Long id) {
        ApplyDeveloper applyDeveloper = applyDeveloperService.selectById(id);
        Integer code = applyDeveloper != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applyDeveloper != null ? "" : "未查询到相关数据";
        return new Result(code, applyDeveloper, msg);
    }

    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody ApplyDeveloper applyDeveloper) {
        boolean flag = applyDeveloperService.updateStatus(applyDeveloper);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }

    @PutMapping("/updateStatus/{id}")
    public Result updateStatusById(@PathVariable Long id) {
        boolean flag = applyDeveloperService.updateStatusById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }
}
