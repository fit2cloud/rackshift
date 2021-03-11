package io.rackshift.controller;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.InstructionDTO;
import io.rackshift.model.ResultHolder;
import io.rackshift.service.InstructionService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@RestController
@RequestMapping("instruction")
public class InstructionController {
    @Resource
    private InstructionService instructionService;

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody InstructionDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, instructionService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody InstructionDTO queryVO) {
        return ResultHolder.success(instructionService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody InstructionDTO queryVO) {
        return ResultHolder.success(instructionService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(instructionService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(instructionService.del(ids));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("delLog")
    public ResultHolder delLog(@RequestBody String[] ids) {
        return ResultHolder.success(instructionService.delLog(ids));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("logs")
    public ResultHolder logs(@RequestParam String id, @RequestBody InstructionDTO instructionDTO) {
        return ResultHolder.success(instructionService.logs(id, instructionDTO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("runCommands")
    public ResultHolder runCommands(@RequestBody InstructionDTO instructionDTO) {
        return ResultHolder.success(instructionService.runCommands(instructionDTO));
    }

}
