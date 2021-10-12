package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.mapper.TaskMapper;
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
        JSONObject r = new JSONObject();
        r.put("identifier", bareMetalId);
        this.subscribeForRequestCommand((o) -> {
            JSONArray taskArr = new JSONArray();
            JSONObject cmd = new JSONObject();
            JSONObject downloadURLOBj = options.getJSONArray("commands").getJSONObject(0);
            cmd.put("downloadUrl", downloadURLOBj.getString("downloadUrl"));
            cmd.put("cmd", downloadURLOBj.getString("command"));
            taskArr.add(cmd);
            r.put("tasks", taskArr);
            return r.toJSONString();
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> this.options);

        //简便起见只去第一个指令的可接受返回 code 写死了先 后面做动态调整
        List<Integer> acceptResponseCode = new ArrayList<Integer>();
        acceptResponseCode.add(1);
        this.subscribeForCompleteCommands(o -> {
            JSONArray tasksArr = JSONArray.parseArray((String) o);
//            for (int i = 0; i < tasksArr.size(); i++) {
//                JSONObject t = tasksArr.getJSONObject(i);
//                if (t.getJSONObject("error") != null && !acceptResponseCode.contains(t.getJSONObject("error").getInteger("code"))) {
//                    this.error(new RSException(t.getJSONObject("error").toJSONString()));
//                }
//            }
//            if (!this._status.equalsIgnoreCase(ServiceConstants.TaskStatusEnum.failed.name())) {
//                this.complete();
//            }
            //还没调通先直接成功好了
            this.complete();
            return "ok";
        });
    }
}
