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
import java.util.ArrayList;
import java.util.List;

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

        JSONObject defaults = JSONObject.parseObject(task.getParams()).getJSONObject("options").getJSONObject("defaults");
        List ipArrayLst = new ArrayList<String>();
        if (defaults.containsKey("networkDevices")) {
            defaults.getJSONArray("networkDevices").forEach(d -> {
                if (((JSONObject) d).containsKey("ipv4")) {
                    ipArrayLst.add(((JSONObject) d).getJSONObject("ipv4").getString("ipAddr"));
                }
            });
        }
        if (defaults.containsKey("bonds")) {
            defaults.getJSONArray("bonds").forEach(d -> {
                if (((JSONObject) d).containsKey("ipv4")) {
                    ipArrayLst.add(((JSONObject) d).getJSONObject("ipv4").getString("ipAddr"));
                }
            });
        }
        String ipArray = String.join(",", ipArrayLst);
        if (result) {
            bareMetal.setIpArray(ipArray);
            bareMetal.setStatus(LifeStatus.deployed.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.succeeded.name());
        } else {
            bareMetal.setStatus(LifeStatus.ready.name());
            task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
        }
        bareMetalManager.update(bareMetal, true);
        taskService.update(task);
        notifyWebSocket(bareMetal, task);
    }
}
