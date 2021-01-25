package io.rackshift.strategy.statemachine.handler.param;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

@Service
public class LinuxHandler implements IOsParamHandler {
    @Override
    public void process(JSONObject params) {
        //装机超时时间设定为2小时 "_taskTimeout": 3600000
        params.getJSONObject("options").put("_taskTimeout", 7200000);

    }
}
