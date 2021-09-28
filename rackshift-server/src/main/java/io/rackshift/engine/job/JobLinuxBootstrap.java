package io.rackshift.engine.job;

import com.alibaba.fastjson.JSON;
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

@Jobs("Job.Linux.Bootstrap")
public class JobLinuxBootstrap extends BaseJob {
    public JobLinuxBootstrap() {

    }

    public JobLinuxBootstrap(String taskId, String instanceId, JSONObject context, TaskMapper taskMapper, ApplicationContext applicationContext, RabbitTemplate rabbitTemplate) {
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
            return r;
        });

        this.subscribeForRequestProfile(o -> this.options.getString("profile"));

        this.subscribeForRequestOptions(o -> this.options);

        this.subscribeForCompleteCommands(o -> {
            this.succeeded();
            return null;
        });
    }

    private class OBMService {
        private String action;

        private OBMService(String action) {
            this.action = action;
        }

        private void run() throws Exception {
            OutBandService outBandService = (OutBandService) applicationContext.getBean("outBandService");
            OutBand outBand = outBandService.getByBareMetalId(bareMetalId);
            if (outBand == null) {
                RSException.throwExceptions("no obm info set!");
            }
            IPMIUtil.Account account = IPMIUtil.Account.build(outBand);
            switch (action) {
                case "setBootPxe":
                    IPMIUtil.exeCommand(account, "chassis setbootdev pxe");
                    break;

                case "reboot":
                    IPMIUtil.exeCommand(account, "chassis power off");
                    Thread.sleep(3000);
                    IPMIUtil.exeCommand(account, "chassis power on");
                    break;
                default:
                    break;
            }
        }
    }
}
