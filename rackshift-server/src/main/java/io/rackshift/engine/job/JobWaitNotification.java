package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.JSONUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Jobs("Job.Wait.Notification")
public class JobWaitNotification extends BaseJob {
    public JobWaitNotification() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobWaitNotification(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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

        //如果跳过 callback 装机直接成功
        SystemParameter waitOsCallback = applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("wait_os_callback");
        if (waitOsCallback != null && !waitOsCallback.getParamValue().equalsIgnoreCase(Boolean.TRUE.toString())) {
            this.complete();
            return;
        }

        this.subscribeForRequestOptions(o -> JSONUtils.merge(this.options, this.renderOptions).toJSONString());
        //success
        this.subscribeForNotification(o -> {
            this.complete();
            return "ok";
        });
    }

}
