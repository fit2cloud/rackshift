package io.rackshift.service;

import io.rackshift.mybatis.domain.OperationLog;
import io.rackshift.mybatis.mapper.OperationLogMapper;
import io.rackshift.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class OperationLogService {
    @Resource
    private OperationLogMapper operationLogMapper;

    public void log(String user, String ipmi命令, String 执行, String s) {
        OperationLog operationLog = new OperationLog();
        operationLog.setId(UUIDUtil.newUUID());
        operationLog.setOperation(执行);
        operationLog.setResourceName(ipmi命令);
        operationLog.setMessage(s);
        operationLog.setTime(System.currentTimeMillis());
        operationLog.setResourceUserId(user);
        operationLogMapper.insertSelective(operationLog);
    }
}
