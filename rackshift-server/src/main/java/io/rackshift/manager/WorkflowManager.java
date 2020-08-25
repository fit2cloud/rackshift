package io.rackshift.manager;

import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.WorkflowParamTemplatesExample;
import io.rackshift.mybatis.domain.WorkflowParamTemplatesWithBLOBs;
import io.rackshift.mybatis.mapper.WorkflowParamTemplatesMapper;
import io.rackshift.utils.SessionUtil;
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

    public WorkflowParamTemplatesWithBLOBs saveWorkflowParamTemplate(WorkflowRequestDTO requestDTO) {
        WorkflowParamTemplatesExample example = new WorkflowParamTemplatesExample();
        example.createCriteria().andBareMetalIdEqualTo(requestDTO.getBareMetalId()).andUserIdEqualTo(SessionUtil.getUser().getId()).andWorkflowNameEqualTo(requestDTO.getWorkflowName());
        List<WorkflowParamTemplatesWithBLOBs> templates = workflowParamTemplatesMapper.selectByExampleWithBLOBs(example);
        WorkflowParamTemplatesWithBLOBs workflowParamTemplates = null;
        if (templates.size() == 0) {
            workflowParamTemplates = new WorkflowParamTemplatesWithBLOBs();
            workflowParamTemplates.setUserId(SessionUtil.getUser().getId());
            workflowParamTemplates.setWorkflowName(requestDTO.getWorkflowName());
            workflowParamTemplates.setBareMetalId(requestDTO.getBareMetalId());
            workflowParamTemplates.setParamsTemplate(requestDTO.getParams().toJSONString());
            workflowParamTemplates.setExtraParams(requestDTO.getExtraParams().toJSONString());
            workflowParamTemplatesMapper.insertSelective(workflowParamTemplates);
        } else {
            workflowParamTemplates = templates.get(0);
            workflowParamTemplates.setUserId(SessionUtil.getUser().getId());
            workflowParamTemplates.setBareMetalId(requestDTO.getBareMetalId());
            workflowParamTemplates.setWorkflowName(requestDTO.getWorkflowName());
            workflowParamTemplates.setParamsTemplate(requestDTO.getParams().toJSONString());
            workflowParamTemplates.setExtraParams(requestDTO.getExtraParams().toJSONString());
            workflowParamTemplatesMapper.updateByPrimaryKeyWithBLOBs(workflowParamTemplates);
        }
        return workflowParamTemplates;
    }
}
