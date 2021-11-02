package io.rackshift.engine.controller;


import io.rackshift.engine.service.ZDHProfileService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class ZDHProfileController {
    @Resource
    private ZDHProfileService zdhProfileService;

    @GetMapping(value = "/api/current/profiles", produces = "text/plain;charset=UTF-8")
    public String profiles(@RequestParam(required = false) String macs) throws IOException, InterruptedException {
        return zdhProfileService.profiles(macs);
    }

    @PostMapping(value = "/api/current/test-profiles")
    public void test(@RequestBody String content, @RequestParam(required = false) boolean test) {
        zdhProfileService.test(content, test);
    }

}
