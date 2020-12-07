package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ImageDTO;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.ImageService;
import io.rackshift.utils.PageUtils;
import io.rackshift.utils.Translator;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

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
        Page<Object> page1 = PageHelper.startPage(page, 1000, true);
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
    public Map upload(@RequestParam(required = false) String endpointId, HttpServletRequest request) {
        boolean isMultipart = ServletFileUpload.isMultipartContent(request);
        if (!isMultipart) {
            RSException.throwExceptions(Translator.get("file-content error!"));
        }
        Map<String, String> r = new HashMap<>();
        String originalName = null;
        String filePath = null;

        ServletFileUpload upload = new ServletFileUpload();
        try {
            FileItemIterator iter = upload.getItemIterator(request);
            while (iter.hasNext()) {
                FileItemStream item = iter.next();
                InputStream stream = item.openStream();
                originalName = item.getName();
                filePath = fileUploadBase + File.separator + originalName;
                if (item.isFormField()) {
                } else {
                    OutputStream out = new FileOutputStream(filePath);
                    IOUtils.copy(stream, out);
                    out.close();
                }
                r.put("originalName", originalName);
                r.put("filePath", filePath);
                //暂时只支持1个iso上传
                return r;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
    }
}
