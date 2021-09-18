package io.rackshift.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@Component
public class TaskGraphListener {
    @Resource
    private RabbitTemplate rabbitTemplate;

    @RabbitListener(queues = {MqConstants.RUN_TASKGRAPH_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void runTaskgraph(JSONObject messageObj) {
        System.out.println(messageObj);
    }

    @RabbitListener(queues = {MqConstants.CANCEL_TASKGRAPH_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void cancelTaskgraph(JSONObject messageObj) {
        System.out.println(messageObj);
    }

    @RabbitListener(queues = {MqConstants.RUN_TASK_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void runTask(JSONObject messageObj) {
        System.out.println(messageObj);
    }

    @RabbitListener(queues = {MqConstants.CANCEL_TASK_QUEUE_NAME}, messageConverter = "mqMessageConverter")
    public void cancelTask(JSONObject messageObj) {
        System.out.println(messageObj);
    }
}
