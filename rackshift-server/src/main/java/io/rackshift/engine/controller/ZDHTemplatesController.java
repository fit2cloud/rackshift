package io.rackshift.engine.controller;


import io.rackshift.engine.service.ZDHProfileService;
import io.rackshift.engine.service.ZDHTemplatesService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.io.IOException;

@RestController
public class ZDHTemplatesController {
    @Resource
    private ZDHTemplatesService zdhTemplatesService;

    @GetMapping(value = "/api/current/templates/{templateName}", produces = "text/plain")
    public String getTemplateName(@PathVariable String templateName, @RequestParam(required = false) String nodeId) throws IOException, InterruptedException {
        return zdhTemplatesService.getTemplateName(templateName, nodeId);
    }
}
