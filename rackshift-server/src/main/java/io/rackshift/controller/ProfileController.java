package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ProfileDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.ProfileService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("profile")
public class ProfileController {

    @Resource
    private ProfileService profileService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody ProfileDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, profileService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody ProfileDTO queryVO) {
        return ResultHolder.success(profileService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody ProfileDTO queryVO) {
        return ResultHolder.success(profileService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(profileService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(profileService.del(ids));
    }

}
