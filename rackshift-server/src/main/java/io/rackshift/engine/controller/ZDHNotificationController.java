package io.rackshift.engine.controller;


import com.alibaba.fastjson.JSONObject;
import io.rackshift.engine.service.ZDHNotificationService;
import io.rackshift.engine.service.ZDHTemplatesService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class ZDHNotificationController {
    @Resource
    private ZDHNotificationService zdhNotificationService;

    @PostMapping(value = {"api/current/notification"})
    public String notification(@RequestParam(required = false) String nodeId, @RequestBody(required = false) JSONObject param) throws Exception {
        return zdhNotificationService.notification(nodeId, param);
    }
}
