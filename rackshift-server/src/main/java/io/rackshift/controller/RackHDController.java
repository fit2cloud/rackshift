package io.rackshift.controller;

import io.rackshift.model.ResultHolder;
import io.rackshift.service.RackHDService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("rackhd")
public class RackHDController {
    @Resource
    private RackHDService rackHDService;

    @RequestMapping("/graphdefinitions/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestParam(required = false) String name) {
        return ResultHolder.success(rackHDService.getGraphDefinitions(name, page, pageSize));
    }
}
