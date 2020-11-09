package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_END)
public class OsWorkflowEndHandler extends AbstractHandler {
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalManager bareMetalManager;
    @Autowired
    private SimpMessagingTemplate template;

    @Override
    public void handleYourself(LifeEvent event) {
        boolean result = event.getWorkflowRequestDTO().getParams().getBoolean("result");

        BareMetal bareMetal = getBareMetalById(event.getBareMetalId());
        TaskWithBLOBs task = taskService.getById(event.getWorkflowRequestDTO().getTaskId());
        bareMetal.setIpArray(JSONObject.parseObject(task.getParams()).getJSONObject("options").getJSONObject("defaults").getJSONArray("networkDevices").getJSONObject(0).getJSONObject("ipv4").getString("ipAddr"));
        if (result) {
            bareMetal.setStatus(LifeStatus.deployed.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.succeeded.name());
        } else {
            bareMetal.setStatus(LifeStatus.ready.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
        }
        bareMetalManager.update(bareMetal, true);
        taskService.update(task);
        template.convertAndSend("/topic/lifecycle", "");
    }
}
