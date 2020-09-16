package io.rackshift.config;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.metal.sdk.util.LogUtil;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.EndpointExample;
import io.rackshift.mybatis.domain.Workflow;
import io.rackshift.mybatis.domain.WorkflowExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
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
    private EndpointMapper endpointMapper;
    private String rackhdUrl;
    private List<Endpoint> endPoints;
    static EndpointMapper staticEndpointMapper;

    @Resource
    private void setEndpointMapper(EndpointMapper endpointMapper) {
        staticEndpointMapper = endpointMapper;
    }

    @PostConstruct
    public void initWorkflow() {
        Map<String, List<Workflow>> typeMap = workflowMapper.selectByExample(new WorkflowExample()).stream().collect(Collectors.groupingBy(Workflow::getEventType));
        if (typeMap != null && typeMap.keySet().size() > 0) {
            for (Map.Entry<String, List<Workflow>> wfEntry : typeMap.entrySet()) {
                if (StringUtils.isBlank(wfEntry.getKey())) continue;
                LifeEventType.valueOf(wfEntry.getKey()).addWorkflow(wfEntry.getValue().stream().map(Workflow::getInjectableName).collect(Collectors.toList()));
            }
        }

        EndpointExample e = new EndpointExample();
        e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.name());
        endPoints = endpointMapper.selectByExample(e);
        if (endPoints.size() > 0 && !endPoints.get(0).getIp().contains(":"))
            rackhdUrl = "http://" + endPoints.get(0).getIp() + ":9090";
        else if (endPoints.get(0).getIp().contains(":"))
            rackhdUrl = "http://" + endPoints.get(0).getIp();
        else
            rackhdUrl = "";

        e.clear();
        endPoints = endpointMapper.selectByExample(e);
    }

    public static String geRrackhdUrl(String id) {
        Endpoint endpoint = staticEndpointMapper.selectByPrimaryKey(id);
        if (endpoint != null && !endpoint.getIp().contains(":"))
            return "http://" + endpoint.getIp() + ":9090";
        if (endpoint == null)
            return null;
        return "http://" + endpoint.getIp();
    }

    public String getRackhdUrl() {
        return rackhdUrl;
    }

    public List<Endpoint> getEndPoints() {
        return endPoints;
    }

    @Bean
    public JSONArray allWorkflow() {
        String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.WORKFLOWS, null);
        if (StringUtils.isNotBlank(res)) {
            return JSONArray.parseArray(res);
        }
        LogUtil.info("无法获取endpoint的WORKFLOWS");
        return new JSONArray();
    }

    @Bean
    public JSONArray allTask() {
        String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.TASKS, null);
        if (StringUtils.isNotBlank(res)) {
            return JSONArray.parseArray(res);
        }
        LogUtil.info("无法获取endpoint的TASKS");
        return new JSONArray();
    }
}
