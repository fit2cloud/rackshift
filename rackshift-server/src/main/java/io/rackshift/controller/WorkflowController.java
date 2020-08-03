package io.rackshift.controller;

import io.rackshift.model.ResultHolder;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.service.WorkflowService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("workflow")
public class WorkflowController {
    @Resource
    private WorkflowService workflowService;

    @RequestMapping("/params/{name}")
    public ResultHolder getParamsByName(@PathVariable String name) {
        return workflowService.getParamsByName(name);
    }

    @RequestMapping("/run")
    public ResultHolder run(@RequestBody List<WorkflowRequestDTO> requestDTOs){
        return workflowService.run(requestDTOs);
    }

}
