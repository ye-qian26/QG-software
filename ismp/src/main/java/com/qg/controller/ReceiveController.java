package com.qg.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/receives")
public class ReceiveController {

    @PostMapping("/mac")
    public String receive(@RequestParam String mac) {
        System.out.println("收到MAC: " + mac);
        return mac;
    }
}
