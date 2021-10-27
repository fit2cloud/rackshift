package io.rackshift.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.mybatis.domain.TaskExample;
import io.rackshift.mybatis.domain.TaskWithBLOBs;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.ExceptionUtils;
import io.rackshift.utils.LogUtil;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class NewWorkflowJob {

    @Resource
    private TaskMapper taskMapper;
    @Resource
    private RabbitTemplate rabbitTemplate;

    @Scheduled(fixedDelay = 1 * 30 * 1000)
    public boolean run() {
        try {
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
        te.createCriteria().andStatusEqualTo(ServiceConstants.TaskStatusEnum.created.name()).andBeforeStatusNotEqualTo(LifeStatus.onrack.name());

        te.setOrderByClause("create_time asc");
        taskList = taskMapper.selectByExampleWithBLOBs(te);

        Map<String, List<TaskWithBLOBs>> groupTaskList = taskList.stream().collect(Collectors.groupingBy(Task::getBareMetalId));
        for (Map.Entry<String, List<TaskWithBLOBs>> entry : groupTaskList.entrySet()) {
            boolean activeTask = findActiveTaskById(entry.getKey());
            //保证同一台机器同一时刻只能有一个任务处于执行状态8
            if (!activeTask) {
//                stateMachine.runTaskList(entry.getValue());
                JSONObject body = new JSONObject();
                body.put("taskId", entry.getValue().get(0).getId());
                Message message = new Message(body.toJSONString().getBytes(StandardCharsets.UTF_8));
                rabbitTemplate.send(MqConstants.RUN_TASKGRAPH_QUEUE_NAME, message);
            }
        }
    }

    private boolean findActiveTaskById(String key) {
        TaskExample te = new TaskExample();
        te.createCriteria().andBareMetalIdEqualTo(key).andStatusEqualTo(ServiceConstants.TaskStatusEnum.running.name());
        List<Task> tasks = taskMapper.selectByExample(te);
        return (tasks.size() > 0 ? true : false);
    }
}
