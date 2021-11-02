package io.rackshift.strategy.statemachine.handler.param;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.Endpoint;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WindowsHandler implements IOsParamHandler {
    @Value("${file.server}")
    private String fileServer;
    @Value("${file.server.port}")
    private String fileServerPort;

    @Override
    public void process(JSONObject params) {
        //装机超时时间设定为2小时 "_taskTimeout": 3600000
        params.getJSONObject("options").put("_taskTimeout", 7200000);
        String winPeUrl = String.format("http://%s:%s/common/winpe", fileServer, fileServerPort);
        params.getJSONObject("options").getJSONObject("defaults").put("repo", winPeUrl);
        JSONArray networkDevices = params.getJSONObject("options").getJSONObject("defaults").getJSONArray("networkDevices");
        if (networkDevices.size() > 0) {
            for (int i = 0; i < networkDevices.size(); i++) {
                if (StringUtils.isNotBlank(networkDevices.getJSONObject(i).getString("device"))) // windows 的坑 必须要转换
                    networkDevices.getJSONObject(i).put("device", networkDevices.getJSONObject(i).getString("device").replace(":", "-"));
            }
        }
    }
}
