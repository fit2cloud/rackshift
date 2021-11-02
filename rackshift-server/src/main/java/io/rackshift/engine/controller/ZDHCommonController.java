package io.rackshift.engine.controller;

import io.rackshift.model.ResultHolder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ZDHCommonController {
    @Value("${file.server}")
    private String fileServer;
    @Value("${file.server.port}")
    private String fileServerPort;

    @RequestMapping("api/current/winpeURL")
    public ResultHolder winpeURL() {
        return ResultHolder.success(String.format("http://%s:%s/common/winpe", fileServer, fileServerPort));
    }
}
