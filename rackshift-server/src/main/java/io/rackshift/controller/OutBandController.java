package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.OutBandDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.service.OutBandService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("outband")
public class OutBandController {
    @Resource
    private OutBandService outBandService;

    @PostMapping("/save")
    public ResultHolder save(@RequestBody OutBand outBand, @RequestParam String bareMetalId) {
        outBandService.fillOBMS(bareMetalId, outBand);
        return ResultHolder.success("");
    }

    @PostMapping("/saveBatch")
    public ResultHolder saveBatch(@RequestBody OutBandDTO outBand) {
        outBandService.fillOBMS(outBand);
        return ResultHolder.success("");
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody OutBandDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, outBandService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody OutBandDTO queryVO) {
        return ResultHolder.success(outBandService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody OutBandDTO queryVO) {
        return ResultHolder.success(outBandService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(outBandService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(outBandService.del(ids));
    }
}