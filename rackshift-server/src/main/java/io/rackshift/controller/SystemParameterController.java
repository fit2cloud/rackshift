package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.SystemParameterDTO;
import io.rackshift.service.SystemParameterService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("system_parameter")
public class SystemParameterController {

    @Resource
    private SystemParameterService systemParameterService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody SystemParameterDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, systemParameterService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody SystemParameterDTO queryVO) {
        return ResultHolder.success(systemParameterService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody SystemParameterDTO queryVO) {
        return ResultHolder.success(systemParameterService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(systemParameterService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(systemParameterService.del(ids));
    }
}
