package io.rackshift.service;

import io.rackshift.constants.ExecutionLogConstants;
import io.rackshift.model.ExecutionLogDTO;
import io.rackshift.model.ExecutionLogDetailsDTO;
import io.rackshift.mybatis.domain.ExecutionLog;
import io.rackshift.mybatis.domain.ExecutionLogDetails;
import io.rackshift.mybatis.domain.ExecutionLogDetailsExample;
import io.rackshift.mybatis.domain.ExecutionLogExample;
import io.rackshift.mybatis.mapper.ExecutionLogDetailsMapper;
import io.rackshift.mybatis.mapper.ExecutionLogMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.SessionUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ExecutionLogService {
    @Resource
    private ExecutionLogMapper executionLogMapper;
    @Resource
    private ExecutionLogDetailsMapper executionLogDetailsMapper;

    public Object add(ExecutionLogDTO queryVO) {
        ExecutionLog image = new ExecutionLog();
        BeanUtils.copyBean(image, queryVO);
        executionLogMapper.insertSelective(image);
        return true;
    }

    public ExecutionLog saveLog(String status) {
        ExecutionLog log = new ExecutionLog();
        log.setCreateTime(System.currentTimeMillis());
        log.setUser(SessionUtil.getUser().getId());
        log.setStatus(status);
        executionLogMapper.insertSelective(log);
        return log;
    }

    public ExecutionLogDetails saveLogDetail(String logId, String user, String operation, String bareMetalId, String status, String outPut) {
        ExecutionLogDetails log = new ExecutionLogDetails();
        log.setCreateTime(System.currentTimeMillis());
        log.setUser(user);
        log.setBareMetalId(bareMetalId);
        log.setLogId(logId);
        log.setOperation(operation);
        log.setStatus(status);
        log.setOutPut(outPut);
        executionLogDetailsMapper.insertSelective(log);
        return log;
    }

    public Object update(ExecutionLogDTO queryVO) {
        ExecutionLog image = new ExecutionLog();
        BeanUtils.copyBean(image, queryVO);
        executionLogMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public Object del(String id) {
        executionLogMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<ExecutionLog> list(ExecutionLogDTO queryVO) {
        ExecutionLogExample example = buildExample(queryVO);
        example.setOrderByClause("create_time desc");
        return executionLogMapper.selectByExample(example);
    }

    private ExecutionLogExample buildExample(ExecutionLogDTO queryVO) {
       ExecutionLogExample e = new ExecutionLogExample();
        if (StringUtils.isNotBlank(queryVO.getUser())) {
            e.createCriteria().andUserEqualTo(queryVO.getUser());

        }
        return e;
    }

    public void finish(String executionLogId) {
        update(executionLogId, ExecutionLogConstants.END);
    }

    public void update(String executionLogId, String status) {
        ExecutionLog executionLog = executionLogMapper.selectByPrimaryKey(executionLogId);
        executionLog.setStatus(ExecutionLogConstants.END);
        executionLogMapper.updateByPrimaryKeySelective(executionLog);
    }

    public void error(String executionId) {
        update(executionId, ExecutionLogConstants.ERROR);
    }

    public List<ExecutionLogDetails> listDetails(ExecutionLogDetailsDTO queryVO) {
        ExecutionLogDetailsExample e = new ExecutionLogDetailsExample();
        if (StringUtils.isNotBlank(queryVO.getBareMetalId())) {
            e.createCriteria().andBareMetalIdEqualTo(queryVO.getBareMetalId());
        }
        e.setOrderByClause("create_time desc");
        return executionLogDetailsMapper.selectByExampleWithBLOBs(e);
    }

    public List<ExecutionLogDetails> listDetailsById(String id) {
        ExecutionLogDetailsExample e = new ExecutionLogDetailsExample();
        e.createCriteria().andLogIdEqualTo(id);
        e.setOrderByClause("create_time desc");
        return executionLogDetailsMapper.selectByExampleWithBLOBs(e);
    }
}
