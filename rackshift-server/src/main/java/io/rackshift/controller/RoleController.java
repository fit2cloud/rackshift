package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.RoleDTO;
import io.rackshift.service.RoleService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("role")
public class RoleController {
    @Resource
    private RoleService roleService;

    @RequestMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody RoleDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, roleService.list(queryVO, page, pageSize)));
    }

    @RequestMapping("add")
    public ResultHolder add(@RequestBody RoleDTO queryVO) {
        return ResultHolder.success(roleService.add(queryVO));
    }

    @RequestMapping("update")
    public ResultHolder update(@RequestBody RoleDTO queryVO) {
        return ResultHolder.success(roleService.update(queryVO));
    }

    @RequestMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(roleService.del(id));
    }

    @RequestMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(roleService.del(ids));
    }
}
