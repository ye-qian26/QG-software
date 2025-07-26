package com.qg.controller;

import com.qg.domain.Ban;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.BanService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/bans")
public class BanController {

    @Autowired
    private BanService banService;

    /**
     * 查询 所有 冻结账户 信息
     * @return
     */
    @GetMapping()
    public Result getAll(){
        List<Ban> bans = banService.selectAll();
        Integer code = bans != null && !bans.isEmpty() ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = bans != null && !bans.isEmpty() ? "" : "暂时未有相关数据";
        return new Result(code, bans, msg);
    }

    /**
     * 添加 冻结账户 信息
     * @param ban
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody Ban ban){

        System.out.println("bans/add ===>>> " + ban);
        boolean flag = banService.add(ban);
        Integer code = flag ? Code.SUCCESS : Code.CONFLICT;
        String msg = flag ? "" : "冻结账户失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 删除 冻结账户 信息
     * @param ban
     * @return
     */
    @DeleteMapping
    public Result delete(@RequestBody Ban ban){
        System.out.println(ban.getUserId());
        boolean flag = banService.delete(ban);
        Integer code = flag ? Code.SUCCESS : Code.INTERNAL_ERROR;
        String msg = flag ? "" : "删除信息失败，请稍后重试！";
        return new Result(code, msg);
    }

    /**
     * 根据 userId 查询 冻结账户
     * @param userId
     * @return
     */
    @GetMapping("/selectByUserId/{userId}")
    public Result selectByUserId(@PathVariable Long userId){
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        Ban ban = banService.selectByUserId(userId);
        Integer code = ban != null ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = ban != null ? "" : "该用户账户未被冻结";
        return new Result(code, ban, msg);
    }

    /**
     * 根据 userId 判断账户 是否 被冻结
     * @param userId
     * @return
     */
    @GetMapping("/judgeBan/{userId}")
    public Result judgeBan(@PathVariable Long userId){
        if (userId == null) {
            return new Result(Code.BAD_REQUEST, "请求参数错误");
        }
        boolean flag = banService.judgeBan(userId);
        Integer code = flag ? Code.SUCCESS : Code.NOT_FOUND;
        String msg = flag ? "" : "该用户账户未被冻结";
        return new Result(code, msg);
    }
}
