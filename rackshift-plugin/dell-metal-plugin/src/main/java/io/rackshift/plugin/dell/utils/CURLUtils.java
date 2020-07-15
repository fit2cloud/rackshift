package io.rackshift.plugin.dell.utils;

import io.rackshift.metal.sdk.util.ExceptionUtils;
import io.rackshift.metal.sdk.util.LogUtil;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CURLUtils {
    public static Result execCurl(String[] cmd) {
        ProcessBuilder processBuilder = new ProcessBuilder(cmd);
        Process p = null;
        Result result = new Result();
        try {
            p = processBuilder.start();
            BufferedReader reader = new BufferedReader(new InputStreamReader(p.getInputStream()));
            StringBuilder builder = new StringBuilder();
            String line = null;
            boolean firstFlag = true;
            boolean headerFlag = true;
            Map<String, String> header = new HashMap<>();
            while ((line = reader.readLine()) != null) {
                if (firstFlag) {
                    firstFlag = false;
                    if (!line.equals("") && line.contains("20")) {
                        result.setCode(200);
                        continue;
                    }
                }
                if (headerFlag && line.contains(":")) {
                    String[] tmp = line.split(":");
                    header.put(tmp[0].trim().toLowerCase(), tmp[1].trim());
                }
                if (line.equals("")) {
                    result.setHeaderStr(builder.toString());
                    result.setHeader(header);
                    builder = new StringBuilder();
                    headerFlag = false;
                }
                builder.append(line);
                builder.append(System.getProperty("line.separator"));
            }
            LogUtil.info(String.format("curl %s 执行结果:%s", Arrays.toString(cmd), builder.toString()));
            result.setResultStr(builder.toString().trim());
            return result;
        } catch (Exception e) {
            LogUtil.info(String.format("curl %s 执行失败！:%s", Arrays.toString(cmd), ExceptionUtils.getExceptionDetail(e)));
        } finally {
            if (p != null) {
                p.destroy();
            }
        }
        return result;
    }
}
