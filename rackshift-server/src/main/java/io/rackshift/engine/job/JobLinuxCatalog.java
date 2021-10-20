package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.service.OutBandService;
import io.rackshift.utils.IPMIUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Jobs("Job.Linux.Catalog")
public class JobLinuxCatalog extends BaseJob {
    public JobLinuxCatalog() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobLinuxCatalog(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
        JSONObject r = new JSONObject();
        r.put("identifier", bareMetalId);
        this.subscribeForRequestCommand((o) -> {
            JSONArray taskArr = new JSONArray();
            JSONObject cmd = new JSONObject();
            cmd.put("cmd", options.getJSONArray("commands"));
            taskArr.add(cmd);
            r.put("tasks", taskArr);
            return r.toJSONString();
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> this.options.toJSONString());

        this.subscribeForCompleteCommands(o -> {
            this.complete();
            return "ok";
        });
    }
}
