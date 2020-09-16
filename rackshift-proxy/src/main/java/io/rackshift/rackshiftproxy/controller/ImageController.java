package io.rackshift.rackshiftproxy.controller;

import io.rackshift.rackshiftproxy.model.R;
import io.rackshift.rackshiftproxy.service.ImageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.io.File;

@Controller("image")
@RequestMapping("image")
public class ImageController {
    @Resource
    private ImageService imageService;

    @RequestMapping(value = "/mount")
    @ResponseBody
    public R mount(@RequestParam String filePath, @RequestParam String mountPath) {
        if (System.getProperty("os.name").toLowerCase().indexOf("linux") != -1) {
            filePath = filePath.replace("\\", "/");
            mountPath = mountPath.replace("\\", "/");
        }
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
                if (!imageService.mountISO(filePath, mountPath)) {
                    return R.failWithMsg("挂载失败！");
                }
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
                if (!imageService.umountISO(filePath, mountPath)) {
                    return R.failWithMsg("挂载失败！");
                }
            } else {
                return R.failWithMsg("只支持linux系统的挂载！");
            }

        } catch (Exception e) {
            return R.failWithMsg("文件挂载失败！");
        }
        return R.successWithData("");
    }
}
