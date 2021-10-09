package io.rackshift.engine.service;

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
        return new String(MqUtil.request(MqConstants.EXCHANGE_NAME, "methods.requestCommands." + bareMetalId, bareMetalId));
    }
}
