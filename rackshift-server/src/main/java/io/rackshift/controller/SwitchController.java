package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.BareMetalQueryVO;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.SwitchQueryVO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Switch;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.SwitchService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("switch")
public class SwitchController {
    @Resource
    private SwitchService switchService;

    @PostMapping("/list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody SwitchQueryVO queryVO) {
        Page<Switch> pager = PageHelper.startPage(page, pageSize);
        return ResultHolder.success(PageUtils.setPageInfo(pager, switchService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(switchService.del(ids));
    }
}
