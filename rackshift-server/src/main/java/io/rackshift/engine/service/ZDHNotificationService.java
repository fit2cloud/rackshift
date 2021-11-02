package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.model.RSException;
import io.rackshift.utils.MqUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class ZDHNotificationService {
    public String notification(String nodeId, JSONObject param) throws Exception {
        if (StringUtils.isBlank(nodeId) && !param.containsKey("nodeId")) {
            RSException.throwExceptions("notification failed!");
        }
        String id = nodeId == null ? param.getString("nodeId") : nodeId;
        return MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_NOTIFICATION + id, "");
    }
}
