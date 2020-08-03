package io.rackshift.manager;

import io.rackshift.mybatis.domain.WorkflowParamTemplatesExample;
import io.rackshift.mybatis.domain.WorkflowParamTemplatesWithBLOBs;
import io.rackshift.mybatis.mapper.WorkflowParamTemplatesMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class WorkflowManager {
    @Resource
    private WorkflowParamTemplatesMapper workflowParamTemplatesMapper;

    public List<WorkflowParamTemplatesWithBLOBs> getParamsByName(String name) {
        WorkflowParamTemplatesExample example = new WorkflowParamTemplatesExample();
        example.createCriteria().andWorkflowNameLike(name);
        return workflowParamTemplatesMapper.selectByExampleWithBLOBs(example);
    }

    public List<WorkflowParamTemplatesWithBLOBs> getUserParamsByName(String name, String user) {
        WorkflowParamTemplatesExample example = new WorkflowParamTemplatesExample();
        example.createCriteria().andWorkflowNameLike(name).andUserIdEqualTo(user);
        return workflowParamTemplatesMapper.selectByExampleWithBLOBs(example);
    }
}
