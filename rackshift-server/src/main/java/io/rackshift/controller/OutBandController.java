package io.rackshift.controller;

import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OutBandService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("outband")
public class OutBandController {
    @Resource
    private OutBandService outBandService;

    @RequestMapping("/save")
    public ResultHolder save(@RequestBody OutBand outBand, @RequestParam String bareMetalId) {
        outBandService.fillOBMS(bareMetalId, outBand);
        return ResultHolder.success("");
    }
}