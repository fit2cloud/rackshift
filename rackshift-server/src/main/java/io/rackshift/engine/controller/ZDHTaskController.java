package io.rackshift.engine.controller;


import com.alibaba.fastjson.JSONObject;
import io.rackshift.engine.service.ZDHTaskService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class ZDHTaskController {
    @Resource
    private ZDHTaskService zdhTaskService;

    @GetMapping(value = "/api/current/tasks/{bareMetalId}", produces = "text/plain")
    public String tasks(@PathVariable String bareMetalId) throws IOException, InterruptedException {
        return zdhTaskService.tasks(bareMetalId);
    }

    @GetMapping(value = "/api/current/tasks/bootstrap.js", produces = "text/plain")
    public String bootstrap(@RequestParam String macAddress) throws IOException, InterruptedException {
        return zdhTaskService.bootstrap(macAddress);
    }

    @PostMapping(value = "/api/current/tasks/{bareMetalId}", produces = "text/plain")
    public void postTasks(@RequestBody JSONObject data, @PathVariable String bareMetalId) throws IOException, InterruptedException {
        zdhTaskService.postTasks(bareMetalId, data);
    }
}
