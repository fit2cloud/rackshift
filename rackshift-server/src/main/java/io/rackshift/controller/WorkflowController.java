package io.rackshift.controller;

import io.rackshift.model.ResultHolder;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.service.WorkflowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("workflow")
public class WorkflowController {
    @Resource
    private WorkflowService workflowService;

    @GetMapping("/params/{name}")
    public ResultHolder getParamsByName(@PathVariable String name) {
        return workflowService.getParamsByName(name);
    }

    @PostMapping("/params")
    public ResultHolder postParamsByName(@RequestBody WorkflowRequestDTO requestDTO) {
        return workflowService.postParamsByName(requestDTO);
    }

    @RequestMapping("/run")
    public ResultHolder run(@RequestBody List<WorkflowRequestDTO> requestDTOs){
        return workflowService.run(requestDTOs);
    }

}
