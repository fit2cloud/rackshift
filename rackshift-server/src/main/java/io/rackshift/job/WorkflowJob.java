package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.ExecutionLogDetailsMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.EndpointService;
import io.rackshift.service.RackHDService;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.LogUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
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
    @Resource
    private ExecutionLogDetailsMapper executionLogDetailsMapper;

    /**
     * 任务执行
     * 1.查询更新正在运行中的任务
     * 2.运行新创建的任务
     * 2.1 按照裸金属分组
     * 2.2 检查本地和RackHD有无正在运行的任务
     * 2.3 如果没有则创建运行任务
     */
    @Scheduled(fixedDelay = 1 * 30 * 1000)
    public boolean run() {
        try {
            updateRunningTask();
            runCreatedTask();
        } catch (Exception e) {
            LogUtil.error("执行任务失败！", ExceptionUtils.getExceptionDetail(e));
            return false;
        }
        return true;
    }

    private void runCreatedTask() {
        List<TaskWithBLOBs> taskList;
        TaskExample te = new TaskExample();
        te.createCriteria().andStatusEqualTo(ServiceConstants.TaskStatusEnum.created.name());
        te.setOrderByClause("create_time asc");
        taskList = taskMapper.selectByExampleWithBLOBs(te);

        Map<String, List<TaskWithBLOBs>> groupTaskList = taskList.stream().collect(Collectors.groupingBy(Task::getBareMetalId));
        for (Map.Entry<String, List<TaskWithBLOBs>> entry : groupTaskList.entrySet()) {
            boolean activeTask = findActiveTaskById(entry.getKey());
            //保证同一台机器同一时刻只能有一个任务处于执行状态
            if (!activeTask) {
                stateMachine.runTaskList(entry.getValue());
            }
        }
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
                String status = rackHDService.getWorkflowStatusById(endpoint, task.getInstanceId());
                //更新日志
                writeDetailLog(endpoint, task);
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

    /**
     * 同步详细日志
     *
     * @param endpoint
     * @param task
     */
    private void writeDetailLog(Endpoint endpoint, Task task) {
        JSONObject runningWorkflow = rackHDService.getWorkflowById(endpoint, task.getInstanceId());
        if (runningWorkflow.containsKey("tasks")) {
            JSONArray tasks = orderedTask(runningWorkflow.getJSONArray("tasks"));
            for (int i = 0; i < tasks.size(); i++) {
                JSONObject taskObj = tasks.getJSONObject(i);
                ExecutionLogDetailsExample e = new ExecutionLogDetailsExample();
                e.createCriteria().andInstanceIdEqualTo(taskObj.getString("instanceId"));
                List<ExecutionLogDetails> logs = executionLogDetailsMapper.selectByExampleWithBLOBs(e);
                ExecutionLogDetails ex = new ExecutionLogDetails();

                if (logs.size() == 0) {
                    ex.setBareMetalId(task.getBareMetalId());
                    ex.setInstanceId(taskObj.getString("instanceId"));
                    ex.setLogId(task.getId());
                    ex.setOperation(taskObj.getString("label"));
                    ex.setStatus(taskObj.getString("state"));
                    setSubTaskOutput(taskObj, ex);
                    executionLogDetailsMapper.insertSelective(ex);
                } else {
                    BeanUtils.copyBean(ex, logs.get(0));
                    setSubTaskOutput(taskObj, ex);
                    executionLogDetailsMapper.updateByPrimaryKeyWithBLOBs(ex);
                }
            }
        }
    }

    private JSONArray orderedTask(JSONArray tasks) {
        JSONArray r = new JSONArray();
        tasks.forEach(t -> {
            if (((JSONObject) t).containsKey("waitingOn")) {
                if (((JSONObject) t).getJSONObject("waitingOn").size() == 0) {
                    r.add(t);
                }
            }
        });
        while (r.size() != tasks.size()) {
            for (int i = 0; i < tasks.size(); i++) {
                if (tasks.getJSONObject(i).getJSONObject("waitingOn").keySet().contains(r.getJSONObject(r.size() - 1).getString("instanceId"))) {
                    r.add(tasks.getJSONObject(i));
                }
            }
        }

        return r;
    }

    private void setSubTaskOutput(JSONObject taskObj, ExecutionLogDetails ex) {
        if (ServiceConstants.RackHDTaskStatusEnum.failed.name().equalsIgnoreCase(taskObj.getString("state"))) {
            if (StringUtils.isNotBlank(taskObj.getString("error")))
                ex.setOutPut("子任务:" + taskObj.getString("label") + " 执行失败！详情：" + taskObj.getString("error"));
            else
                ex.setOutPut("子任务:" + taskObj.getString("label") + " 执行失败！");
        } else if (ServiceConstants.RackHDTaskStatusEnum.succeeded.name().equalsIgnoreCase(taskObj.getString("state"))) {
            ex.setOutPut("子任务:" + taskObj.getString("label") + " 执行成功！");
        } else if (ServiceConstants.RackHDTaskStatusEnum.cancelled.name().equalsIgnoreCase(taskObj.getString("state"))) {
            ex.setOutPut("子任务:" + taskObj.getString("label") + "已取消");
        } else if (ServiceConstants.RackHDTaskStatusEnum.timeout.name().equalsIgnoreCase(taskObj.getString("state"))) {
            ex.setOutPut("子任务:" + taskObj.getString("label") + "已超时");
        } else {
            ex.setOutPut("子任务:" + taskObj.getString("label") + " 已提交,等待执行...");
        }
        ex.setStatus(taskObj.getString("state"));
    }
}
