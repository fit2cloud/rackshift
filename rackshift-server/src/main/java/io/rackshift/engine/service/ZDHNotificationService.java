package io.rackshift.engine.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.MqConstants;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.Catalog;
import io.rackshift.mybatis.mapper.CatalogMapper;
import io.rackshift.utils.MqUtil;
import io.rackshift.utils.UUIDUtil;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.crypto.engines.RSAEngine;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.Optional;

@Service
public class ZDHNotificationService {
    public String notification(String nodeId, JSONObject param) throws Exception {
        if (StringUtils.isBlank(nodeId) && !param.containsKey("nodeId")) {
            RSException.throwExceptions("notification failed!");
        }
        String id = Optional.ofNullable(nodeId).orElse(param.getString("nodeId"));
        return MqUtil.request(MqConstants.EXCHANGE_NAME, MqConstants.MQ_ROUTINGKEY_NOTIFICATION + id, "");
    }
}
