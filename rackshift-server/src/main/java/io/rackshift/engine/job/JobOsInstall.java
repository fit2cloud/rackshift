package io.rackshift.engine.job;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.JSONUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;

@Jobs("Job.Os.Install")
public class JobOsInstall extends BaseJob {
    public JobOsInstall() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobOsInstall(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
            cmd.put("cmd", "");
            taskArr.add(cmd);
            r.put("tasks", taskArr);
            return r.toJSONString();
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> {
            JSONObject options = JSONUtils.merge(this.options, this.renderOptions);
            if (options.containsKey("rootPassword")) {
                //encry password for linux
                try {
                    options.put("rootEncryptedPassword", encrypt(options.getString("rootPassword")));
                    options.put("rootPlainPassword", options.getString("rootPassword"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return options.toJSONString();
        });

        this.subscribeForCompleteCommands(o -> {
            this.complete();
            return "ok";
        });

        this.subscribeForNotification(o -> {
            this.complete();
            return "ok";
        });
    }

    private static String encrypt(String rootPassword) throws IOException {
        ProcessBuilder processBuilder = new ProcessBuilder("python3", "-c", "import crypt;print(crypt.crypt(\"" + rootPassword + "\"));");
        processBuilder.redirectErrorStream(true);
        Process p = processBuilder.start();
        InputStreamReader re = new InputStreamReader(p.getInputStream(), "utf-8");
        BufferedReader b = new BufferedReader(re);
        String line;
        StringBuffer sb = new StringBuffer();
        while ((line = b.readLine()) != null) {
            sb.append(line + "\n");
        }
        p.getInputStream().close();
        re.close();
        b.close();
        p.destroy();
        return sb.toString();
    }
}
