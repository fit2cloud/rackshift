package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.TemplateDTO;
import io.rackshift.service.TemplateService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("template")
public class TemplateController {

    @Resource
    private TemplateService templateService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody TemplateDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, templateService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @GetMapping("getAllTemplates")
    public ResultHolder getAllTemplates() {
        return ResultHolder.success(templateService.getAllTemplates());
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody TemplateDTO queryVO) throws Exception {
        return ResultHolder.success(templateService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody TemplateDTO queryVO) throws Exception {
        return ResultHolder.success(templateService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(templateService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(templateService.del(ids));
    }

}
