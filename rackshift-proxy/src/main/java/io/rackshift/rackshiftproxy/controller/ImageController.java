package io.rackshift.rackshiftproxy.controller;

import com.alibaba.fastjson.JSONObject;
import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DockerClientBuilder;
import io.rackshift.rackshiftproxy.model.R;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.Date;

@Controller("image")
@RequestMapping("image")
public class ImageController {

    @RequestMapping(value = "/test")
    @ResponseBody
    public String test() {
        DockerClient client = DockerClientBuilder.getInstance().build();
        return JSONObject.toJSONString(client.listImagesCmd().exec());
    }

    @RequestMapping(value = "/mount")
    @ResponseBody
    public R mount(@RequestParam String filePath, @RequestParam String mountPath) {
        File uploadedFile = new File(filePath);
        File mountDir = new File(mountPath);
        if (!uploadedFile.exists()) {
            return R.failWithMsg("文件不存在！");
        }
        if (!uploadedFile.getName().endsWith("iso")) {
            return R.failWithMsg("文件不是ISO格式！");
        }
        if (!mountDir.exists()) {
            if (!mountDir.mkdirs()) {
                return R.failWithMsg("创建挂载目录失败！");
            }
        }
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(String.format("echo >> %s %s iso9660 ro 0 0", filePath, mountDir));
                runtime.exec(String.format("mount -a"));
            } else {
                return R.failWithMsg("只支持linux系统的挂载！");
            }

        } catch (Exception e) {
            return R.failWithMsg("文件挂载失败！");
        }
        return R.successWithData("");
    }

    @RequestMapping(value = "/umount")
    @ResponseBody
    public R umount(@RequestParam String filePath, @RequestParam String mountPath) {
        File uploadedFile = new File(filePath);
        File mountDir = new File(mountPath);
        if (!uploadedFile.exists()) {
            return R.failWithMsg("文件不存在！");
        }
        if (!uploadedFile.getName().endsWith("iso")) {
            return R.failWithMsg("文件不是ISO格式！");
        }
        if (!mountDir.exists()) {
            return R.failWithMsg("挂载目录不存在！");
        }
        try {
            if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
                Runtime runtime = Runtime.getRuntime();
                runtime.exec(String.format("umount %s", mountDir));
                runtime.exec(String.format("sed -i '/%s/d' /etc/fstab", uploadedFile.getName()));
            } else {
                return R.failWithMsg("只支持linux系统的挂载！");
            }

        } catch (Exception e) {
            return R.failWithMsg("文件挂载失败！");
        }
        return R.successWithData("");
    }
}
