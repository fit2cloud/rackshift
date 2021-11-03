package io.rackshift.service;

import io.rackshift.constants.ServiceConstants;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.manager.WorkflowManager;
import io.rackshift.model.RSException;
import io.rackshift.model.ResultHolder;
import io.rackshift.model.WorkflowDTO;
import io.rackshift.model.WorkflowRequestDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.domain.WorkflowWithBLOBs;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class WorkflowService {
    @Resource
    private WorkflowManager workflowManager;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private StateMachine stateMachine;
    @Resource
    private WorkflowMapper workflowMapper;
    @Resource
    private TaskService taskService;

    public ResultHolder getParamsByName(String name) {
        return ResultHolder.success(workflowManager.getParamsByName(name));
    }

    public ResultHolder run(List<WorkflowRequestDTO> requestDTOs) {
        if (requestDTOs.size() == 0) {
            return ResultHolder.success("");
        }
        Map<String, List<WorkflowRequestDTO>> machineWfMap = requestDTOs.stream().collect(Collectors.groupingBy(WorkflowRequestDTO::getBareMetalId));
        machineWfMap.keySet().forEach(m -> {
            List<LifeEvent> events = new LinkedList<>();
            for (WorkflowRequestDTO requestDTO : machineWfMap.get(m)) {
                String bareMetalId = requestDTO.getBareMetalId();
                String workflowName = requestDTO.getWorkflowName();
                if (StringUtils.isAnyBlank(bareMetalId, workflowName)) {
                    RSException.throwExceptions(Translator.get("error"));
                }
                BareMetal bareMetal = bareMetalManager.getBareMetalById(bareMetalId);
                if (bareMetal == null) {
                    RSException.throwExceptions(Translator.get("error"));
                }

                events.add(LifeEvent.builder().withWorkflowRequestDTO(requestDTO).withEventType(LifeEventType.fromStartType(workflowName)));
            }
            //插入数据库等待执行
            taskService.createTaskFromEvents(events);
        });

        return ResultHolder.success("");
    }

    public ResultHolder postParamsByName(WorkflowRequestDTO requestDTO) {
        return ResultHolder.success(workflowManager.saveWorkflowParamTemplate(requestDTO));
    }

    public ResultHolder listall() {
        WorkflowExample e = new WorkflowExample();
        e.createCriteria().andStatusEqualTo(ServiceConstants.ENABLE);
        return ResultHolder.success(workflowMapper.selectByExampleWithBLOBs(e));
    }

    public List<WorkflowWithBLOBs> list(WorkflowDTO queryVO) {
        WorkflowExample workflowExample = buildExample(queryVO);
        return workflowMapper.selectByExampleWithBLOBs(workflowExample);
    }

    private WorkflowExample buildExample(WorkflowWithBLOBs queryVO) {
        WorkflowExample e = new WorkflowExample();

        if (queryVO.getId() != null) {
            e.or().andIdEqualTo(queryVO.getId());
        }
        return e;
    }

    public boolean add(WorkflowDTO queryVO) {
        WorkflowWithBLOBs workflow = new WorkflowWithBLOBs();
        BeanUtils.copyBean(workflow, queryVO);
        workflowMapper.insertSelective(workflow);
        return true;
    }

    public boolean update(WorkflowWithBLOBs queryVO) {
        WorkflowExample workflowExample = buildExample(queryVO);
        workflowMapper.updateByExampleWithBLOBs(queryVO, workflowExample);
        return true;
    }

    public boolean del(String id) {
        Workflow w = workflowMapper.selectByPrimaryKey(id);
        if (w != null && ServiceConstants.SYSTEM.equalsIgnoreCase(w.getType())) {
            return false;
        }
        workflowMapper.deleteByPrimaryKey(id);
        return true;
    }

    public boolean del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return true;
    }

    public ResultHolder listallEventType() {

        return ResultHolder.success(LifeEventType.getVisableTypes());
    }

    public String getFriendlyName(String workflowId) {
        return workflowManager.getFriendlyName(workflowId);
    }

    public Workflow getById(String workFlowId) {
        return workflowMapper.selectByPrimaryKey(workFlowId);
    }

    public WorkflowWithBLOBs getByInjectableName(String injectableName) {
        WorkflowExample e = new WorkflowExample();
        e.createCriteria().andInjectableNameEqualTo(injectableName);
        List<WorkflowWithBLOBs> r = workflowMapper.selectByExampleWithBLOBs(e);
        return r.size() > 0 ? r.get(0) : null;
    }
}
