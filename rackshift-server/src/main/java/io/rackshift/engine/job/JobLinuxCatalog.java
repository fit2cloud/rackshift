package io.rackshift.engine.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.engine.util.CatalogParser;
import io.rackshift.engine.util.CommandParser;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.MachineEntity;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.mapper.TaskMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.utils.JSONUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.ApplicationContext;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
        List cmdList = cmds.stream().map(c -> {
            if (c instanceof JSONObject && ((JSONObject) c).containsKey("command"))
                return ((JSONObject) c).getString("command");
            return c;
        }).collect(Collectors.toList());
        JSONObject r = new JSONObject();
        r.put("identifier", bareMetalId);
        this.subscribeForRequestCommand((o) -> {
            JSONArray taskArr = new JSONArray();
            cmdList.forEach(c -> {
                JSONObject cmd = new JSONObject();
                cmd.put("cmd", c);
                taskArr.add(cmd);
            });
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
                if (tasksObj == null || tasksObj.size() == 0) {
                    this.complete();
                    return "ok";
                }
                JSONObject taskObj = tasksObj.getJSONObject(0);
                String stderr = taskObj.getString("stderr");
                if (StringUtils.isNotBlank(stderr)) {
                    this.error(new RSException(stderr));
                    return "ok";
                }

                cp.saveCatalog(bareMetalId, tasksObj);
            } catch (IOException e) {
                e.printStackTrace();
                this.error(new RSException("save catalog error!" + e.getMessage()));
                return "ok";
            }
            refreshNodeHardWare();
            this.complete();
            return "ok";
        });
    }

    private void refreshNodeHardWare() {
        CatalogParser catalogParser = (CatalogParser) applicationContext.getBean("catalogParser");
        BareMetalManager bareMetalManager = (BareMetalManager) applicationContext.getBean("bareMetalManager");
        SimpMessagingTemplate template = applicationContext.getBean(SimpMessagingTemplate.class);
        BareMetal bm = bareMetalManager.getBareMetalById(bareMetalId);
        LifeEvent event = LifeEvent.builder().withBareMetalId(bareMetalId);
        MachineEntity en = catalogParser.parse(event);
        en.setStatus(bm.getStatus());
        try {
            bareMetalManager.saveOrUpdateEntity(en);
            template.convertAndSend("/topic/lifecycle", "");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
