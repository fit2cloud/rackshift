package io.rackshift.strategy.statemachine;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.engine.job.BaseJob;
import io.rackshift.model.RSException;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.ExecutionLogService;
import io.rackshift.service.TaskService;
import io.rackshift.service.WorkflowService;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.lang.reflect.Constructor;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class StateMachine {
    @Resource
    private ExecutionLogService executionLogService;
    @Resource
    private TaskService taskService;
    @Resource
    private WorkflowService workflowService;
    @Resource
    private Map<String, Class> job;
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private RabbitTemplate rabbitTemplate;

    private ConcurrentHashMap<LifeEventType, IStateHandler> handlerMap = new ConcurrentHashMap<>();
    private static List<String> endStatusList = new ArrayList<String>() {{
        add(ServiceConstants.TaskStatusEnum.cancelled.name());
        add(ServiceConstants.TaskStatusEnum.failed.name());
        add(ServiceConstants.TaskStatusEnum.succeeded.name());
    }};

    public void configureHandler(LifeEventType event, IStateHandler handler) {
        handlerMap.put(event, handler);
    }

    /**
     * 运行一个完整的 task 工作流
     *
     * @param taskId
     */
    public void runTaskGraph(String taskId) {
        runTaskList(Arrays.asList(Optional.ofNullable(taskService.getById(taskId)).orElse(new TaskWithBLOBs())));
    }

    public void runTaskList(List<TaskWithBLOBs> tasks) {
        tasks.forEach(task -> {
            if (StringUtils.isBlank(task.getId())) return;
            LifeEvent event = buildEvent(task);
            if (task != null) {
                Task preTask = taskService.getById(task.getPreTaskId());
                //等待前置任务处理结束才能执行
                if (preTask == null || (preTask != null && endStatusList.contains(preTask.getStatus()))) {
                    task.setStatus(ServiceConstants.TaskStatusEnum.running.name());
                    taskService.update(task);
                    executionLogService.saveLogDetail(task.getId(), task.getUserId(), ExecutionLogConstants.OperationEnum.START.name(), event.getBareMetalId(), String.format("开始执行:%s:%s", event.getEventType().getDesc(), Optional.ofNullable(event.getWorkflowRequestDTO().getWorkflowName()).orElse("无")));
                    if (handlerMap.get(event.getEventType()) != null) {
                        handlerMap.get(event.getEventType()).handle(event);
                    } else {
                        LogUtil.error(String.format("不支持的事件类型！%s", JSON.toJSONString(event)));
                    }
                }
            }
        });
    }

    public void sendEvent(LifeEvent event) {
        handlerMap.get(event.getEventType()).handleNoSession(event);
    }

    private LifeEvent buildEvent(TaskWithBLOBs task) {
        Workflow workflow = workflowService.getById(task.getWorkFlowId());
        if (workflow == null) {
            RSException.throwExceptions(String.format("not exist workflow %s", task.getWorkFlowId()));
        }
        LifeEventType type = LifeEventType.fromStartType(workflow.getInjectableName());
        WorkflowRequestDTO requestDTO = new WorkflowRequestDTO();
        requestDTO.setBareMetalId(task.getBareMetalId());
        requestDTO.setTaskId(task.getId());
        requestDTO.setWorkflowName(workflow.getInjectableName());
        requestDTO.setParams(JSONObject.parseObject(Optional.ofNullable(task.getParams()).orElse("{}")));
        requestDTO.setExtraParams(JSONObject.parseObject(Optional.ofNullable(task.getExtparams()).orElse("{}")));
        return LifeEvent.builder().withEventType(type).withWorkflowRequestDTO(requestDTO);
    }

    /**
     * 运行数据库 task 表中 graphobject 的子任务
     *
     * @param messageObj
     */
    public void runTask(JSONObject messageObj) {
        String taskId = messageObj.getString("taskId");
        String instanceId = messageObj.getString("instanceId");
        if (StringUtils.isAnyBlank(taskId, instanceId)) {
            return;
        }
        TaskWithBLOBs task = taskService.getById(taskId);
        JSONObject graphObject = JSONObject.parseObject(task.getGraphObjects());
        String runJob = graphObject.getJSONObject(instanceId).getString("runJob");
        if (StringUtils.isNotBlank(runJob)) {
            Class c1 = job.get(runJob);
            if (c1 != null) {
                try {
                    Constructor c = c1.getConstructor(String.class, String.class, JSONObject.class, TaskMapper.class, ApplicationContext.class, RabbitTemplate.class);
                    if (c != null) {
                        BaseJob bj = (BaseJob) c.newInstance(taskId, instanceId, graphObject.getJSONObject(instanceId), taskMapper, applicationContext, rabbitTemplate);
                        bj.init();
                        bj.run();
                    }
                } catch (Exception e) {
                    LogUtil.error("子任务：" + graphObject.getJSONObject(instanceId) + "运行失败！");
                }
            }
        }
    }
}
