package io.rackshift.controller;

import com.alibaba.fastjson.JSONArray;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.WorkflowDTO;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.service.WorkflowService;
import io.rackshift.utils.PageUtils;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("workflow")
public class WorkflowController {
    @Resource
    private WorkflowService workflowService;
    @Resource
    private Map taskGraph;

    @GetMapping("/params/{name}")
    public ResultHolder getParamsByName(@PathVariable String name) {
        return workflowService.getParamsByName(name);
    }

    @PostMapping("/params")
    public ResultHolder postParamsByName(@RequestBody WorkflowRequestDTO requestDTO) {
        return workflowService.postParamsByName(requestDTO);
    }

    @PostMapping("/run")
    public ResultHolder run(@RequestBody List<WorkflowRequestDTO> requestDTOs) {
        return workflowService.run(requestDTOs);
    }

    @GetMapping("listall")
    public ResultHolder listall() {
        return workflowService.listall();
    }

    @GetMapping("listallEventType")
    public ResultHolder listallEventType() {
        return workflowService.listallEventType();
    }

    @GetMapping("listallRackHDWorkflows")
    public ResultHolder listallRackHDWorkflows() {
        return ResultHolder.success(taskGraph);
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("list/{page}/{pageSize}")
    public ResultHolder list(@PathVariable int page, @PathVariable int pageSize, @RequestBody WorkflowDTO queryVO) {
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        return ResultHolder.success(PageUtils.setPageInfo(page1, workflowService.list(queryVO)));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("add")
    public ResultHolder add(@RequestBody WorkflowDTO queryVO) {
        return ResultHolder.success(workflowService.add(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PutMapping("update")
    public ResultHolder update(@RequestBody WorkflowDTO queryVO) {
        return ResultHolder.success(workflowService.update(queryVO));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @DeleteMapping("del/{id}")
    public ResultHolder del(@PathVariable String id) {
        return ResultHolder.success(workflowService.del(id));
    }

    @RequiresRoles(AuthorizationConstants.ROLE_ADMIN)
    @PostMapping("del")
    public ResultHolder del(@RequestBody String[] ids) {
        return ResultHolder.success(workflowService.del(ids));
    }

}
