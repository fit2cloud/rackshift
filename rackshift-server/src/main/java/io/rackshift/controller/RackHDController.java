package io.rackshift.controller;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.RackHDService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("rackhd")
public class RackHDController {
    @Resource
    private RackHDService rackHDService;

    @GetMapping("/graphdefinitions/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestParam(required = false) String name) {
        return ResultHolder.success(rackHDService.getGraphDefinitions(name, page, pageSize));
    }

    @GetMapping("/allOsAndVersion")
    public ResultHolder allOsAndVersion() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("os.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuffer sb = new StringBuffer();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        reader.close();
        in.close();
        return ResultHolder.success(JSONArray.parseArray(sb.toString()));
    }
}
