package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.engine.util.CommandParser;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.io.IOException;
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
        JSONArray cmds = options.getJSONArray("commands");
        JSONObject r = new JSONObject();
        r.put("identifier", bareMetalId);
        this.subscribeForRequestCommand((o) -> {
            JSONArray taskArr = new JSONArray();
            JSONObject cmd = new JSONObject();
            cmd.put("cmd", cmds);
            taskArr.add(cmd);
            r.put("tasks", taskArr);
            return r.toJSONString();
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> JSONUtils.merge(this.options, this.renderOptions).toJSONString());

        this.subscribeForCompleteCommands(o -> {
            CommandParser cp = (CommandParser) applicationContext.getBean("commandParser");
            try {
                JSONObject resultObj = JSONObject.parseObject((String) o);
                String bareMetalId = resultObj.getString("identifier");
                JSONArray tasksObj = resultObj.getJSONArray("tasks");
                if (tasksObj == null || tasksObj.size() == 0)
                    return "ok";
                JSONObject taskObj = tasksObj.getJSONObject(0);
                String cmd1 = null;
                if (taskObj.getJSONArray("cmd") != null && taskObj.getJSONArray("cmd").size() > 0) {
                    cmd1 = taskObj.getJSONArray("cmd").getString(0);
                }
                String originalContent = taskObj.getString("stdout");
                String stderr = taskObj.getString("stderr");
                if (StringUtils.isNotBlank(stderr)) {
                    this.error(RSException.throwExceptions(stderr));
                    return "ok";
                }

                cp.saveCatalog(bareMetalId, cmd1, taskObj);
            } catch (IOException e) {
                e.printStackTrace();
                this.error(RSException.throwExceptions("save catalog error!" + e.getMessage()));
                return "ok";
            }
            this.complete();
            return "ok";
        });
    }
}
