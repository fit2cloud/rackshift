package io.rackshift.strategy.statemachine.handler.param;

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

    @PostConstruct
    public void initMap() {
        handlerMap.put("Graph.InstallWindowsServer", windowsHandler);
    }

    public IOsParamHandler getHandler(String osType) {
        return handlerMap.get(osType);
    }
}
