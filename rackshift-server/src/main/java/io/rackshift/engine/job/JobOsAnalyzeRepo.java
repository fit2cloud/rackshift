package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.utils.HttpClientUtil;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;

import java.util.Map;

@Jobs("Job.Os.Analyze.Repo")
public class JobOsAnalyzeRepo extends BaseJob {
    public JobOsAnalyzeRepo() {

    }

    /**
     * @param taskId             task 表的 id
     * @param instanceId         task表中 graphObjct 字段每一个具体子任务的 id
     * @param context            task表中 graphObjct 字段每一个具体子任务的 json 对象
     * @param taskMapper
     * @param applicationContext
     * @param rabbitTemplate
     */
    public JobOsAnalyzeRepo(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
        //validate esxi repo
        //download repo/boot.cfg to get cfg args
        String content = "";
        try {
            content = HttpClientUtil.get(options.getString("repo") + "/boot.cfg", null);
            updateAllOptions(getParam(content));
        } catch (Exception e) {
            this.error(RSException.throwExceptions("get esxi repo boot.cfg failed ! either the repo is not valid nor the file formate is not correct!"));
            return;
        }
        this.complete();
    }

    private JSONObject getParam(String content) {
        JSONArray params = JSONArray.parseArray("[ {\n" +
                "            key: 'tbootFile',\n" +
                "                    pattern: 'kernel='\n" +
                "        }, {\n" +
                "            key: 'moduleFiles',\n" +
                "                    pattern: 'modules='\n" +
                "        }\n" +
                "        ]");
        JSONObject result = new JSONObject();
        params.forEach(p -> {
            result.put(((JSONObject) p).getString("key"), getValue(((JSONObject) p).getString("pattern"), content));
        });

        result.put("mbootFile", "mboot.c32");
        result.put("kargs", "");
        return result;
    }

    private static String getValue(String pattern, String c) {
        int startIndex = c.indexOf(pattern);
        int endIndex = c.substring(startIndex + pattern.length()).indexOf("\n");
        return c.substring(startIndex + pattern.length()).substring(0, endIndex).replaceAll("/", "");
    }
}
