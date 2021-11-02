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

    @GetMapping(value = {"/api/current/templates/{templateName}", "/api/templates/{templateName}", "/api/2.0/templates/{templateName}"}, produces = "text/plain")
    public String getTemplateName(@PathVariable String templateName, @RequestParam(required = false) String nodeId, @RequestParam(required = false) String macs) throws IOException, InterruptedException {
        return zdhTemplatesService.getTemplateName(templateName, nodeId, macs);
    }

    /**
     * only for ubuntu-livecd
     *
     * @param macaddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = {"/api/current/templates/nocloud/{macaddress}/user-data"}, produces = "text/plain")
    public String getLiveCDTemplateUserData(@PathVariable String macaddress) throws IOException, InterruptedException {
        return zdhTemplatesService.getTemplateName("user-data.yaml", null, macaddress);
    }

    /**
     * only for ubuntu-livecd
     *
     * @param macaddress
     * @return
     * @throws IOException
     * @throws InterruptedException
     */
    @GetMapping(value = {"/api/current/templates/nocloud/{macaddress}/meta-data"}, produces = "text/plain")
    public String getLiveCDTemplateMetaData(@PathVariable String macaddress) throws IOException, InterruptedException {
        return "";
    }

}
