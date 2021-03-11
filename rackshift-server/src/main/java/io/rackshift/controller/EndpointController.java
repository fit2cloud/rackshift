package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.EndpointDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.EndpointService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("endpoint")
public class EndpointController {

    @Resource
    private EndpointService endpointService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody EndpointDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, endpointService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @GetMapping("getAllEndPoints")
    public ResultHolder getAllEndPoints() {
        return ResultHolder.success(endpointService.getAllEndPoints());
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @GetMapping("getAllEndPointType")
    public ResultHolder getAllEndPointType() {
        return ResultHolder.success(endpointService.getAllEndPointType());
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody EndpointDTO queryVO) {
        return ResultHolder.success(endpointService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody EndpointDTO queryVO) {
        return ResultHolder.success(endpointService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(endpointService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(endpointService.del(ids));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @GetMapping("sync")
    public ResultHolder sync() {
        return ResultHolder.success(endpointService.sync());
    }
}
