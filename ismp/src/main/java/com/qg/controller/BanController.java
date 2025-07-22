package com.qg.controller;

import com.qg.domain.Ban;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/bans")
public class BanController {

    @Autowired
    private BanService banService;

    @GetMapping()
    public Result getAll(){
        List<Ban> bans = banService.selectAll();
        Integer code = bans != null && !bans.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = bans != null && !bans.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, bans, msg);
    }

    @PostMapping
    public Result add(@RequestBody Ban ban){
        boolean flag = banService.add(ban);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "冻结账户失败，请稍后重试！";
        return new Result(code, msg);
    }

    @DeleteMapping
    public Result delete(@RequestBody Ban ban){
        boolean flag = banService.delete(ban);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除信息失败，请稍后重试！";
        return new Result(code, msg);
    }

    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId){
        Ban ban = banService.selectByUserId(userId);
        Integer code = ban != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = ban != null ? "" : "该用户账户未被冻结";
        return new Result(code, ban, msg);
    }

    @GetMapping("/judgeBan/{userId}")
    public Result judgeBan(@PathVariable Long userId){
        boolean flag = banService.judgeBan(userId);
        Integer code = flag ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = flag ? "" : "该用户账户未被冻结";
        return new Result(code, msg);
    }
}
