package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.SwitchRuleDTO;
import io.rackshift.model.BareMetalRuleVO;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.SwitchRuleDTO;
import io.rackshift.service.DiscoveryService;
import io.rackshift.service.SwitchDiscoveryService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("switch-discovery")
public class SwitchDiscoveryController {

    @Resource
    private SwitchDiscoveryService switchDiscoveryService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody SwitchRuleDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, switchDiscoveryService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody SwitchRuleDTO queryVO) {
        return ResultHolder.success(switchDiscoveryService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody SwitchRuleDTO queryVO) {
        return ResultHolder.success(switchDiscoveryService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(switchDiscoveryService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(switchDiscoveryService.del(ids));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("sync")
    public ResultHolder sync(@RequestBody String[] ids) {
        return ResultHolder.success(switchDiscoveryService.sync(ids));
    }

}
