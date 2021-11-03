package io.rackshift.strategy.statemachine.handler;

import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.service.TaskService;
import io.rackshift.strategy.statemachine.AbstractHandler;
import io.rackshift.strategy.statemachine.EventHandlerAnnotation;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;

import javax.annotation.Resource;

@EventHandlerAnnotation(LifeEventType.POST_DISCOVERY_WORKFLOW_START)
public class DiscoveryStartHandler extends AbstractHandler {
    @Resource
    private TaskService taskService;

    /**
     * 执行发现分为两种情况
     * 1.手动添加的物理机未被rackhd发现
     * 执行发现的workflow：先PXE启动再轮询找到该机器的节点信息，最后刷新
     * 2.已经执行过一次完整的信息采集的机器
     * 先pxe启动rancher再执行发现workflow
     *
     * @param event
     */
    @Override
    public void handleYourself(LifeEvent event) throws Exception {
        String taskId = event.getWorkflowRequestDTO().getTaskId();
        TaskWithBLOBs task = taskService.getById(taskId);
        startTask(task);
    }
}
