package io.rackshift.strategy.statemachine.handler.param;

import com.alibaba.fastjson.JSONObject;

public interface IOsParamHandler {
    void process(JSONObject params);
}
