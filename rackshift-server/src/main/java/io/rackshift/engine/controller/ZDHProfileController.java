package io.rackshift.engine.controller;


import io.rackshift.engine.service.ZDHProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
public class ZDHProfileController {
    @Resource
    private ZDHProfileService zdhProfileService;

    @GetMapping(value = "/api/current/profiles", produces = "text/plain;charset=UTF-8")
//    @GetMapping(value = "/api/current/profiles")
//    @GetMapping(value = "/api/current/profiles", produces = "text/plain;charset=UTF-8", headers = "Transfer-Encoding=chunked")
    public String profiles(@RequestParam(required = false) String macs) {
        return zdhProfileService.profiles(macs);
    }

    @PostMapping(value = "/api/current/test-profiles")
    public void test(@RequestBody String content, @RequestParam(required = false) boolean test){
        zdhProfileService.test(content, test);
    }

}
