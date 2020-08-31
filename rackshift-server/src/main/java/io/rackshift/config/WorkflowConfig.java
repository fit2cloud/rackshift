package io.rackshift.config;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.mapper.WorkflowMapper;
import io.rackshift.strategy.statemachine.LifeEventType;
import org.apache.commons.lang3.StringUtils;
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
                if (StringUtils.isBlank(wfEntry.getKey())) continue;
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }
    }

    @Bean
    public JSONArray allWorkflow() {
        String res = HttpFutureUtils.getHttp(rackhdUrl + RackHDConstants.WORKFLOWS, null);
        if (StringUtils.isNotBlank(res)) {
            LogUtil.info("无法获取endpoint的WORKFLOWS");
            return JSONArray.parseArray(res);
        }
        return new JSONArray();
    }

    @Bean
    public JSONArray allTask() {
        String res = HttpFutureUtils.getHttp(rackhdUrl + RackHDConstants.TASKS, null);
        if (StringUtils.isNotBlank(res)) {
            LogUtil.info("无法获取endpoint的TASKS");
            return JSONArray.parseArray(res);
        }
        return new JSONArray();
    }
}
