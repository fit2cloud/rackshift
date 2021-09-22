package io.rackshift.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.strategy.statemachine.StateMachine;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskGraphListener {
    @Resource
    private StateMachine stateMachine;

    /**
     * 执行工作流
     *
     * @param messageObj
     */
    @RabbitListener(queues = {MqConstants.RUN_TASKGRAPH_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void runTaskgraph(JSONObject messageObj) {
        stateMachine.runTaskGraph(messageObj.getString("taskId"));
    }

    @RabbitListener(queues = {MqConstants.CANCEL_TASKGRAPH_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void cancelTaskgraph(JSONObject messageObj) {
    }

    /**
     * 执行工作流的子任务
     *
     * @param messageObj
     */
    @RabbitListener(queues = {MqConstants.RUN_TASK_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void runTask(JSONObject messageObj) {
        stateMachine.runTask(messageObj);
    }

    @RabbitListener(queues = {MqConstants.CANCEL_TASK_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void cancelTask(JSONObject messageObj) {
    }
}
