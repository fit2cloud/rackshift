package io.rackshift.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
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

    @RequestMapping("/allOsAndVersion")
    public ResultHolder allOsAndVersion() {
        String osAndVersionsStr = "[\n" +
                "  {\n" +
                "\t\"id\": \"centos\",\n" +
                "\t\"name\": \"Centos 64位\",\n" +
                "\t\"versions\": [{\n" +
                "\t\t\t\"name\": \"5\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"6\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7.4\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7.6\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "},\n" +
                "{\n" +
                "\t\"id\": \"redhat\",\n" +
                "\t\"name\": \"RedHat 64位\",\n" +
                "\t\"versions\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}\n" +
                "]\n" +
                "\n";
        return ResultHolder.success(JSONArray.parseArray(osAndVersionsStr));
    }

    public static void main(String[] args) {
        String osAndVersionsStr = "[\n" +
                "  {\n" +
                "\t\"id\": \"centos\",\n" +
                "\t\"name\": \"Centos 64位\",\n" +
                "\t\"versions\": [{\n" +
                "\t\t\t\"name\": \"5\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"6\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7.4\"\n" +
                "\t\t},\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7.6\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "},\n" +
                "{\n" +
                "\t\"id\": \"redhat\",\n" +
                "\t\"name\": \"RedHat 64位\",\n" +
                "\t\"versions\": [\n" +
                "\t\t{\n" +
                "\t\t\t\"name\": \"7\"\n" +
                "\t\t}\n" +
                "\t]\n" +
                "}\n" +
                "]\n" +
                "\n";
        System.out.println(osAndVersionsStr);
    }
}
