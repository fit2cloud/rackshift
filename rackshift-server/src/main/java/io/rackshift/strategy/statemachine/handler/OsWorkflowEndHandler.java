package io.rackshift.strategy.statemachine.handler;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.*;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_OS_WORKFLOW_END)
public class OsWorkflowEndHandler extends AbstractHandler {
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalManager bareMetalManager;

    @Override
    public void handleYourself(LifeEvent event) {
        boolean result = event.getWorkflowRequestDTO().getParams().getBoolean("result");

        BareMetal bareMetal = getBareMetalById(event.getBareMetalId());
        TaskWithBLOBs task = taskService.getById(event.getWorkflowRequestDTO().getTaskId());

        if (result) {
            bareMetal.setIpArray(JSONObject.parseObject(task.getParams()).getJSONObject("options").getJSONObject("defaults").getJSONArray("networkDevices").getJSONObject(0).getJSONObject("ipv4").getString("ipAddr"));
            bareMetal.setStatus(LifeStatus.deployed.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.succeeded.name());
            executionLogService.saveLogDetail(task.getId(), task.getUserId(), ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), String.format("裸金属服务器:%s,部署成功！业务IP:%s", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn(), bareMetal.getIpArray()));
        } else {
            bareMetal.setStatus(LifeStatus.ready.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
            executionLogService.saveLogDetail(task.getId(), task.getUserId(), ExecutionLogConstants.OperationEnum.END.name(), event.getBareMetalId(), String.format("裸金属服务器:%s,部署失败！", bareMetal.getMachineModel() + " " + bareMetal.getMachineSn()));
        }
        bareMetalManager.update(bareMetal, true);
        taskService.update(task);
        notifyWebSocket(bareMetal, task);
    }
}
