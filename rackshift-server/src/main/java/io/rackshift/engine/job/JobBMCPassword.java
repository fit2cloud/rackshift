package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.mybatis.mapper.TaskMapper;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;
import java.util.Optional;

@Jobs("Job.BMC.Password")
public class JobBMCPassword extends BaseJob {
    public JobBMCPassword() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobBMCPassword(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
        SystemParameter createCredential = applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_credentials");
        String userName = Optional.ofNullable(applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_username")).orElse(new SystemParameter()).getParamValue();
        String password = Optional.ofNullable(applicationContext.getBean(SystemParameterMapper.class).selectByPrimaryKey("bmc_password")).orElse(new SystemParameter()).getParamValue();
        if (createCredential != null && "true".equalsIgnoreCase(createCredential.getParamValue())) {
            JSONObject param = new JSONObject();
            param.put("user", Optional.ofNullable(userName).orElse("rackshift"));
            param.put("password", Optional.ofNullable(password).orElse("rackshift"));
            updateAllOptions(param);
        } else {

            //不设置 bmc 跳过下个执行脚本的任务
            JSONObject param = new JSONObject();
            param.put("jumpTask", "set-bmc");
            updateAllOptions(param);
        }
        this.complete();
    }
}
