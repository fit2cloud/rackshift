package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ImageDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.ImageService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

@RestController
@RequestMapping("image")
public class ImageController {

    @Resource
    private ImageService imageService;

    @Value("${file.upload.dir}")
    private String fileUploadBase;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody ImageDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, imageService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("add")
    public ResultHolder add(@RequestBody ImageDTO queryVO) {
        return ResultHolder.success(imageService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("update")
    public ResultHolder update(@RequestBody ImageDTO queryVO) {
        return ResultHolder.success(imageService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(imageService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(imageService.del(ids));
    }

    @ResponseBody
    @RequestMapping(value = "/upload", method = {RequestMethod.POST})
    public String upload(@RequestParam("file") MultipartFile file, @RequestParam(required = false) String endpointId) {
        String originalName = file.getOriginalFilename();
        String path = fileUploadBase + File.separator + originalName;
        try {
            if (!new File(path).exists()) {
                new File(path).createNewFile();
            }
            FileOutputStream fs = new FileOutputStream(path);
            byte[] buffer = new byte[1024 * 1024];
            int byteread = 0;
            InputStream stream = file.getInputStream();
            while ((byteread = stream.read(buffer)) != -1) {
                fs.write(buffer, 0, byteread);
                fs.flush();
            }
            fs.close();
        } catch (Exception e) {
            if (new File(path).exists())
                new File(path).delete();
        }
        return imageService.mount(path, originalName, endpointId);
    }
}
