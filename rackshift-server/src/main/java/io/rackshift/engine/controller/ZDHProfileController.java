package io.rackshift.engine.controller;


import io.rackshift.engine.service.ZDHProfileService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController

public class ZDHProfileController {
    @Resource
    private ZDHProfileService zdhProfileService;

    @GetMapping("api/current/profiles")
    public String profiles(@RequestParam(required = false) String macs) {
        return zdhProfileService.profiles(macs);
    }
}
