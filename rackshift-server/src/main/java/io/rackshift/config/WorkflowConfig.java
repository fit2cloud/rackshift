package io.rackshift.config;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.strategy.statemachine.LifeEventType;
import org.springframework.context.annotation.Bean;
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
    @Resource
    private String rackhdUrl;

    @PostConstruct
    public void initWorkflow() {
        Map<String, List<Workflow>> typeMap = workflowMapper.selectByExample(new WorkflowExample()).stream().collect(Collectors.groupingBy(Workflow::getEventType));
        if (typeMap != null && typeMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Workflow>> wfEntry : typeMap.entrySet()) {
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }
    }

    @Bean
    public JSONArray allWorkflow() {
        return JSONArray.parseArray(HttpFutureUtils.getHttp(rackhdUrl + RackHDConstants.WORKFLOWS, null));
    }

    @Bean
    public JSONArray allTask() {
        return JSONArray.parseArray(HttpFutureUtils.getHttp(rackhdUrl + RackHDConstants.TASKS, null));
    }
}
