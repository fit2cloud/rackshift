package io.rackshift.controller;

import com.github.pagehelper.Page;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.NetworkDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.NetworkService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

@RestController
@RequestMapping("network")
public class NetworkController {

    @Resource
    private NetworkService networkService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody NetworkDTO queryVO) {
        Map r = networkService.list(queryVO, page, pageSize);
        return ResultHolder.success(PageUtils.setPageInfo((Page) r.get("page"), r.get("list")));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody NetworkDTO queryVO) {
        return ResultHolder.success(networkService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody NetworkDTO queryVO) {
        return ResultHolder.success(networkService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(networkService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(networkService.del(ids));
    }
}
