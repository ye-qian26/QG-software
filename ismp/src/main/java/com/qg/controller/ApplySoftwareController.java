package com.qg.controller;

import com.qg.domain.ApplySoftware;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.ApplySoftwareService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/applySoftware")
public class ApplySoftwareController {

    @Autowired
    private ApplySoftwareService applySoftwareService;

    @GetMapping
    public Result selectAllOrderByTime() {
        List<ApplySoftware> applySoftwares = applySoftwareService.selectAllOrderByTime();
        Integer code = applySoftwares != null && !applySoftwares.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftwares != null && !applySoftwares.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, applySoftwares, msg);
    }

    @PostMapping
    public Result add(@RequestBody ApplySoftware applySoftware) {
        boolean flag = applySoftwareService.add(applySoftware);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "添加失败，请稍后重试！";
        return new Result(code, msg);
    }

    @DeleteMapping
    public Result delete(@RequestBody ApplySoftware applySoftware) {
        boolean flag = applySoftwareService.delete(applySoftware);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    @DeleteMapping("/{id}")
    public Result deleteById(@PathVariable Long id) {
        boolean flag = applySoftwareService.deleteById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除失败，请稍后重试！";
        return new Result(code, msg);
    }

    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId) {
        List<ApplySoftware> applySoftwares = applySoftwareService.selectByUserId(userId);
        Integer code = applySoftwares != null && !applySoftwares.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftwares != null && !applySoftwares.isEmpty() ? "" : "未查询到相关数据";
        return new Result(code, applySoftwares, msg);
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Long id) {
        ApplySoftware applySoftware = applySoftwareService.selectById(id);
        Integer code = applySoftware != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = applySoftware != null ? "" : "未查询到相关数据";
        return new Result(code, applySoftware, msg);
    }

    @PutMapping("/updateStatus")
    public Result updateStatus(@RequestBody ApplySoftware applySoftware) {
        boolean flag = applySoftwareService.updateStatus(applySoftware);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }

    @PutMapping("/updateStatus/{id}")
    public Result updateStatusById(@PathVariable Long id) {
        boolean flag = applySoftwareService.updateStatusById(id);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "更改状态失败，请稍后重试！";
        return new Result(code, msg);
    }
}
