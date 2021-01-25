package io.rackshift.strategy.statemachine.handler.param;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@Service
public class AbstractParamHandler {
    private Map<String, IOsParamHandler> handlerMap = new HashMap<>();
    @Resource
    private WindowsHandler windowsHandler;
    @Resource
    private LinuxHandler linuxHandler;

    @PostConstruct
    public void initMap() {
        handlerMap.put("Graph.InstallWindowsServer", windowsHandler);
        handlerMap.put("Graph.InstallCentOS", linuxHandler);
        handlerMap.put("Graph.InstallRHEL", linuxHandler);
    }

    public IOsParamHandler getHandler(String osType) {
        return handlerMap.get(osType);
    }

}
