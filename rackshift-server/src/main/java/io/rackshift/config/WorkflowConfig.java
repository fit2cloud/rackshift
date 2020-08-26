package io.rackshift.config;

import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.strategy.statemachine.LifeEventType;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class WorkflowConfig {
    @Resource
    private WorkflowMapper workflowMapper;

    @PostConstruct
    public void initWorkflow() {
        Map<String, List<Workflow>> typeMap = workflowMapper.selectByExample(new WorkflowExample()).stream().collect(Collectors.groupingBy(Workflow::getEventType));
        if (typeMap != null && typeMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Workflow>> wfEntry : typeMap.entrySet()) {
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }
    }
}
