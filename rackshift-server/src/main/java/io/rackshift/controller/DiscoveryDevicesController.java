package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.DiscoveryDevicesDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.service.DiscoveryDevicesService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("discovery-devices")
public class DiscoveryDevicesController {

    @Resource
    private DiscoveryDevicesService discoveryDevicesService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody DiscoveryDevicesDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, discoveryDevicesService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("add")
    public ResultHolder add(@RequestBody DiscoveryDevicesDTO queryVO) {
        return ResultHolder.success(discoveryDevicesService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("update")
    public ResultHolder update(@RequestBody DiscoveryDevicesDTO queryVO) {
        return ResultHolder.success(discoveryDevicesService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(discoveryDevicesService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(discoveryDevicesService.del(ids));
    }

@RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @RequestMapping("addToBareMetal")
    public ResultHolder addToBareMetal(@RequestBody BareMetalDTO bareMetalDTO) {
        return ResultHolder.success(discoveryDevicesService.addToBareMetal(bareMetalDTO));
    }

}
