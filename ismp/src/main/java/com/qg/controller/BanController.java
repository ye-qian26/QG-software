package com.qg.controller;

import com.qg.domain.Ban;
import com.qg.domain.Code;
import com.qg.domain.Result;
import com.qg.service.BanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/ban")
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


}
