package io.rackshift.controller;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.model.ResultHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@RestController
@RequestMapping("rackhd")
public class RackHDController {

    @GetMapping("/allOsAndVersion")
    public ResultHolder allOsAndVersion() throws IOException {
        InputStream in = this.getClass().getClassLoader().getResourceAsStream("os.json");
        BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
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
