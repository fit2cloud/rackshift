package io.rackshift.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.EndpointService;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Service
public class WorkflowJob {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private BareMetalMapper bareMetalMapper;
    @Resource
    private EndpointService endpointService;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private StateMachine stateMachine;
    private ConcurrentHashMap<String, Boolean> handlingMap = new ConcurrentHashMap<>();

    /**
     * 任务执行
     * 1.查询更新正在运行中的任务
     * 2.运行新创建的任务
     * 2.1 按照裸金属分组
     * 2.2 检查本地和RackHD有无正在运行的任务
     * 2.3 如果没有则创建运行任务
     */
    @Scheduled(fixedDelay = 1 * 60 * 1000)
    public void run() {
        updateRunningTask();
        runCreatedJob();
    }

    private void runCreatedJob() {
        List<TaskWithBLOBs> taskList;
        TaskExample te = new TaskExample();
        te.createCriteria().andStatusEqualTo(ServiceConstants.TaskStatusEnum.created.name());
        te.setOrderByClause("create_time desc");
        taskList = taskMapper.selectByExampleWithBLOBs(te);

        Map<String, List<TaskWithBLOBs>> groupTaskList = taskList.stream().collect(Collectors.groupingBy(Task::getBareMetalId));
        for (Map.Entry<String, List<TaskWithBLOBs>> entry : groupTaskList.entrySet()) {
            boolean activeTask = findActiveTaskById(entry.getKey());
            if (!activeTask) {
                List<TaskWithBLOBs> tasks = removeHandlingTasks(entry.getValue());
                tasks.forEach(t -> handlingMap.put(t.getId(), true));
                stateMachine.sendTaskListAsyn(tasks);
            }
        }
    }

    private List<TaskWithBLOBs> removeHandlingTasks(List<TaskWithBLOBs> value) {
        return value.stream().filter(v -> !Optional.ofNullable(handlingMap.get(v.getId())).orElse(false)).collect(Collectors.toList());
    }

    private boolean findActiveTaskById(String key) {
        TaskExample te = new TaskExample();
        te.createCriteria().andBareMetalIdEqualTo(key).andStatusEqualTo(ServiceConstants.TaskStatusEnum.running.name());
        List<Task> tasks = taskMapper.selectByExample(te);

        BareMetal bareMetal = bareMetalMapper.selectByPrimaryKey(key);
        List<String> nodeIds = rackHDService.getActiveWorkflowNodeIds(bareMetal.getEndpointId());
        return (tasks.size() > 0 ? true : false) || nodeIds.contains(bareMetal.getServerId());
    }

    private void updateRunningTask() {
        TaskExample te = new TaskExample();
        te.createCriteria().andStatusEqualTo(ServiceConstants.TaskStatusEnum.running.name());
        List<Task> taskList = taskMapper.selectByExample(te);
        for (Task task : taskList) {
            if (StringUtils.isNotBlank(task.getInstanceId())) {
                BareMetal b = bareMetalMapper.selectByPrimaryKey(task.getBareMetalId());
                Endpoint endpoint = endpointService.getById(b.getEndpointId());
                String status = rackHDService.getWorkflowStatusById("http://" + endpoint.getIp() + ":9090", task.getInstanceId());
                //运行结束
                if (!ServiceConstants.TaskStatusEnum.running.name().equals(status)) {
                    WorkflowRequestDTO requestDTO = new WorkflowRequestDTO();
                    requestDTO.setTaskId(task.getId());
                    if (ServiceConstants.TaskStatusEnum.cancelled.name().equals(status) || ServiceConstants.TaskStatusEnum.failed.name().equals(status)) {
                        requestDTO.setParams(JSONObject.parseObject("{ \"result\" : false}"));

                    } else if (ServiceConstants.TaskStatusEnum.succeeded.name().equals(status)) {
                        requestDTO.setParams(JSONObject.parseObject("{ \"result\" : true}"));
                    }
                    requestDTO.setBareMetalId(task.getBareMetalId());
                    LifeEventType type = LifeEventType.fromEndType(task.getWorkFlowId());
                    LifeEvent event = LifeEvent.builder().withEventType(type);
                    event.withWorkflowRequestDTO(requestDTO);
                    stateMachine.sendEvent(event);
                }
            } else {
                task.setStatus(ServiceConstants.TaskStatusEnum.failed.name());
                task.setUpdateTime(System.currentTimeMillis());
                taskMapper.updateByPrimaryKey(task);
            }
        }
    }
}
