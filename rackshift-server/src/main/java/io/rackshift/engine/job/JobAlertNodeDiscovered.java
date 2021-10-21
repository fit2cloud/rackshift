package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Jobs("Job.Alert.Node.Discovered")
public class JobAlertNodeDiscovered extends BaseJob {
    public JobAlertNodeDiscovered() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobAlertNodeDiscovered(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
        this.instanceId = instanceId;
        this.taskId = taskId;
        this.context = context;
        this.options = context.getJSONObject("options");
        this._status = context.getString("state");
        this.taskMapper = taskMapper;
        this.task = taskMapper.selectByPrimaryKey(taskId);
        this.bareMetalId = context.getString("bareMetalId");
        this.applicationContext = applicationContext;
        this.job = (Map<String, Class>) applicationContext.getBean("job");
        this.rabbitTemplate = rabbitTemplate;
    }

    @Override
    public void run() {
        this.complete();
        WorkflowRequestDTO r = new WorkflowRequestDTO();
        r.setBareMetalId(bareMetalId);
        r.setTaskId(taskId);
        LifeEvent event = LifeEvent.builder().withEventType(LifeEventType.POST_DISCOVERY_WORKFLOW_END).withWorkflowRequestDTO(r);
        StateMachine stateMachine = (StateMachine) applicationContext.getBean("stateMachine");
        stateMachine.sendEvent(event);
    }
}
