package io.rackshift.rackshiftproxy.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("endpoint")
public class EndpointController {

    @RequestMapping("heartBeat")
    public String heartBeat() {
        return "ok";
    }
}
