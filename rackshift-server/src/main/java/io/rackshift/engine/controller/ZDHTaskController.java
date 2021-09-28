package io.rackshift.engine.controller;


import io.rackshift.engine.service.ZDHTaskService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ZDHTaskController {
    @Resource
    private ZDHTaskService zdhTaskService;

    @GetMapping(value = "/api/current/tasks/{bareMetalId}", produces = "text/plain")
    public String tasks(@PathVariable String bareMetalId) {
        return zdhTaskService.tasks(bareMetalId);
    }
}
