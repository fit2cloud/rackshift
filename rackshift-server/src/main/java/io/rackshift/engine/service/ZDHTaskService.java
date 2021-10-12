package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.service.BareMetalService;
import io.rackshift.service.ProfileService;
import io.rackshift.service.TaskService;
import io.rackshift.utils.MqUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;

@Service
public class ZDHTaskService {
    @Resource
    private ProfileService profileService;
    @Resource
    private TaskService taskService;
    @Resource
    private BareMetalService bareMetalService;

    public String tasks(String bareMetalId) throws IOException, InterruptedException {
        return MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMMANDS + bareMetalId, bareMetalId);
    }

    public void postTasks(String bareMetalId, JSONObject data) throws IOException, InterruptedException {
        MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_COMPLETE + bareMetalId, data.toJSONString());
    }
}
