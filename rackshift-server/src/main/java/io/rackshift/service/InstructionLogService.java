package io.rackshift.service;

import io.rackshift.mybatis.domain.InstructionLogExample;
import io.rackshift.mybatis.domain.OperationLog;
import io.rackshift.mybatis.mapper.InstructionLogMapper;
import io.rackshift.mybatis.mapper.OperationLogMapper;
import io.rackshift.utils.UUIDUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class InstructionLogService {
    @Resource
    private InstructionLogMapper instructionLogMapper;

    public void delDetailsByBareMetalId(String id) {
        InstructionLogExample e = new InstructionLogExample();
        e.createCriteria().andBareMetalIdEqualTo(id);
        instructionLogMapper.deleteByExample(e);
    }
}
