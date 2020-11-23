package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.RSException;
import io.rackshift.model.TaskDTO;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.ExecutionLogDetailsMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.mybatis.mapper.ext.ExtTaskMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.SessionUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskService {
    @Resource
    private TaskMapper taskMapper;
    @Resource
    private ExtTaskMapper extTaskMapper;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private StateMachine stateMachine;
    @Resource
    private ExecutionLogDetailsMapper executionLogDetailsMapper;
    @Resource
    private EndpointService endpointService;

    public Object add(TaskDTO queryVO) {
        TaskWithBLOBs task = new TaskWithBLOBs();
        BeanUtils.copyBean(task, queryVO);

        taskMapper.insertSelective(task);
        return true;
    }

    public Object update(Task queryVO) {
        taskMapper.updateByPrimaryKey(queryVO);
        return true;
    }

    public Object del(String id) {
        Task task = taskMapper.selectByPrimaryKey(id);
        if (task == null) return false;
        if (bareMetalManager.getBareMetalById(task.getBareMetalId()) == null || StringUtils.isBlank(task.getInstanceId())) {
            taskMapper.deleteByPrimaryKey(id);
            return true;
        }
        if (StringUtils.isNotBlank(task.getInstanceId())) {
            //rackhd 清除 任务
            Endpoint endpoint = endpointService.getById(bareMetalManager.getBareMetalById(task.getBareMetalId()).getEndpointId());
            if (endpoint == null) {
                taskMapper.deleteByPrimaryKey(id);
                return true;
            }
            String status = rackHDService.getWorkflowStatusById(endpoint, task.getInstanceId());
            if (!ServiceConstants.TaskStatusEnum.running.name().equalsIgnoreCase(status)) {
                taskMapper.deleteByPrimaryKey(id);
                return true;
            }
            boolean r = rackHDService.cancelWorkflow(bareMetalManager.getBareMetalById(task.getBareMetalId()), task.getInstanceId());
            if (r) {
                WorkflowRequestDTO requestDTO = new WorkflowRequestDTO();
                requestDTO.setTaskId(task.getId());
                requestDTO.setParams(JSONObject.parseObject("{ \"result\" : false}"));

                requestDTO.setBareMetalId(task.getBareMetalId());
                LifeEventType type = LifeEventType.fromEndType(task.getWorkFlowId());
                LifeEvent event = LifeEvent.builder().withEventType(type);
                event.withWorkflowRequestDTO(requestDTO);
                stateMachine.sendEvent(event);

                taskMapper.deleteByPrimaryKey(id);
            } else {
                RSException.throwExceptions("取消任务失败！instanceID：" + task.getInstanceId());
            }
        }
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Map> list(TaskDTO queryVO) {
        return extTaskMapper.list(queryVO);
    }

    private TaskExample buildExample(TaskDTO queryVO) {
        return new TaskExample();
    }

    public void createTaskFromEvents(List<LifeEvent> events) {
        Map<String, List<LifeEvent>> eventsMap = groupByBM(events);
        for (Map.Entry<String, List<LifeEvent>> entry : eventsMap.entrySet()) {
            List<TaskWithBLOBs> list = new ArrayList();
            entry.getValue().forEach(e -> {
                TaskWithBLOBs task = new TaskWithBLOBs();
                task.setId(UUIDUtil.newUUID());
                task.setBareMetalId(entry.getKey());
                task.setWorkFlowId(e.getWorkflowRequestDTO().getWorkflowName());
                task.setUserId(SessionUtil.getUser().getId());
                task.setParams(Optional.ofNullable(e.getWorkflowRequestDTO().getParams()).orElse(new JSONObject()).toJSONString());
                task.setExtparams(Optional.ofNullable(e.getWorkflowRequestDTO().getExtraParams()).orElse(new JSONObject()).toJSONString());
                task.setStatus(ServiceConstants.TaskStatusEnum.created.name());
                task.setCreateTime(System.currentTimeMillis());
                list.add(task);
            });

            BareMetal bareMetal = bareMetalManager.getBareMetalById(entry.getKey());
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    list.get(i).setPreTaskId(list.get(i - 1).getId());
                }
                list.get(i).setBeforeStatus(bareMetal.getStatus());
            }
            //第一个任务默认以之前的任意一个未结束任务为前置任务 如果全部都已经结束，则该任务为当前第一个执行任务
            list.get(0).setPreTaskId(findLastTaskId(list.get(0).getBareMetalId()));
            for (TaskWithBLOBs taskWithBLOBs : list) {
                taskMapper.insertSelective(taskWithBLOBs);
            }
        }
    }

    private String findLastTaskId(String bareMetalId) {
        TaskExample e = new TaskExample();
        e.createCriteria().andBareMetalIdEqualTo(bareMetalId).andStatusEqualTo(ServiceConstants.TaskStatusEnum.created.name());
        e.or().andBareMetalIdEqualTo(bareMetalId).andStatusEqualTo(ServiceConstants.TaskStatusEnum.running.name());
        List<Task> tasks = taskMapper.selectByExample(e);
        return tasks.size() > 0 ? tasks.get(0).getId() : null;
    }

    private Map<String, List<LifeEvent>> groupByBM(List<LifeEvent> events) {
        return events.stream().collect(Collectors.groupingBy(LifeEvent::getBareMetalId));
    }

    public TaskWithBLOBs getById(String taskId) {
        return taskMapper.selectByPrimaryKey(taskId);
    }

    public Object logs(String id) {
        ExecutionLogDetailsExample e = new ExecutionLogDetailsExample();
        e.createCriteria().andLogIdEqualTo(id);
        e.setOrderByClause("create_time asc");
        return executionLogDetailsMapper.selectByExampleWithBLOBs(e);
    }

    public int delByBareMetalId(String id) {
        TaskExample e = new TaskExample();
        e.createCriteria().andBareMetalIdEqualTo(id);
        return taskMapper.deleteByExample(e);
    }


    public static void main(String[] args) {
        new ArrayList<>().get(0);
    }
}
