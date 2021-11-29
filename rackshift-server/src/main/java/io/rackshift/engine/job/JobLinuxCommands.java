package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Jobs("Job.Linux.Commands")
public class JobLinuxCommands extends BaseJob {
    public JobLinuxCommands() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobLinuxCommands(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
        //支持直接跳过执行命令的子任务提高效率
        if (context.getString("label").equalsIgnoreCase(options.getString("jumpTask"))) {
            JSONObject thisTask = getTaskByInstanceId(instanceId);
            thisTask.put("info", String.format("jump this task : %s", options.getString("jumpTask")));
            setTask(thisTask);
            this.complete();
            return;
        }

        JSONObject r = new JSONObject();
        r.put("identifier", bareMetalId);
        this.subscribeForRequestCommand((o) -> {
            JSONArray taskArr = new JSONArray();
            JSONObject cmd = new JSONObject();
            if (options.get("commands") instanceof JSONArray) {
                Object commandObj = options.getJSONArray("commands").get(0);
                if (commandObj instanceof String) {
                    cmd.put("cmd", commandObj);
                } else if (commandObj instanceof JSONObject) {
                    JSONObject downloadURLOBj = options.getJSONArray("commands").getJSONObject(0);
                    if (downloadURLOBj.containsKey("downloadUrl"))
                        cmd.put("downloadUrl", downloadURLOBj.getString("downloadUrl"));
                    if (downloadURLOBj.containsKey("command"))
                        cmd.put("cmd", downloadURLOBj.getString("command"));
                }
            } else if (options.get("commands") instanceof String) {
                cmd.put("cmd", options.getString("commands"));
            }
            taskArr.add(cmd);
            r.put("tasks", taskArr);
            return r.toJSONString();
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> JSONUtils.merge(this.options, this.renderOptions).toJSONString());

        //简便起见只去第一个指令的可接受返回 code 写死了先 后面做动态调整
        List<Integer> acceptResponseCode = new ArrayList<Integer>();
        acceptResponseCode.add(1);
        this.subscribeForCompleteCommands(o -> {
            if (StringUtils.isNotBlank((String) o) && ((String) o).contains("error")) {
                this.error(new RSException((String) o));
            } else {
                this.complete();
            }
            return "ok";
        });
    }
}
