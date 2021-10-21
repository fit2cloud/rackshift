package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Task;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import io.rackshift.utils.MqUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.List;

@Service
public class ZDHTaskService {
    @Resource
    private ProfileService profileService;
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalService bareMetalService;
    @Resource
    private ZDHTemplatesService zdhTemplatesService;

    public String tasks(String bareMetalId) throws IOException, InterruptedException {
        List<Task> taskList = taskService.getActiveTasks(bareMetalId);
        if (taskList.size() > 0) {
            return MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMMANDS + bareMetalId, bareMetalId);
        }
        return "no active tasks!";
    }

    public void postTasks(String bareMetalId, JSONObject data) throws IOException, InterruptedException {
        List<Task> taskList = taskService.getActiveTasks(bareMetalId);
        if (taskList.size() > 0) {
            MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMPLETE + bareMetalId, data.toJSONString());
        }
    }

    public String bootstrap(String macAddress) throws IOException, InterruptedException {
        BareMetal bareMetal = bareMetalService.getByPXEMAC(macAddress);
        if (bareMetal != null)
            return zdhTemplatesService.getTemplateName("bootstrap.js", bareMetal.getId());
        return "";
    }
}
