package io.rackshift.config;

import com.alibaba.fastjson.JSONArray;
import io.rackshift.constants.RackHDConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
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
import java.net.Inet4Address;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
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
        else if (endPoints.size() > 0 && endPoints.get(0).getIp().contains(":"))
            rackhdUrl = "http://" + endPoints.get(0).getIp();
        else
            rackhdUrl = "";

        e.clear();
        endPoints = endpointMapper.selectByExample(e);
        endPoints = endPoints.stream().map(endpoint -> {
            if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
                try {
                    endpoint.setIp(getGateWayAddress(""));
                } catch (Exception unknownHostException) {
                    unknownHostException.printStackTrace();
                }
            }
            return endpoint;
        }).collect(Collectors.toList());
    }

    private static String getGateWayAddress(String ip) throws UnknownHostException, SocketException {
        String localhost = Inet4Address.getLocalHost().getHostAddress();
        if (StringUtils.isNotBlank(ip)) {
            localhost = ip;
        }
        int maskLength = NetworkInterface.getByInetAddress(Inet4Address.getLocalHost()).getInterfaceAddresses().get(0).getNetworkPrefixLength();
        StringBuffer sb = new StringBuffer();
        StringBuffer number = new StringBuffer();

        int i = 0;
        while (i < 32) {
            if (i < maskLength) {
                sb.append("1");
            }
            if ((i + 1) % 8 == 0) {
                number.append(Integer.valueOf(sb.toString().equals("") ? "0" : sb.toString()));
                sb = new StringBuffer();
                if (i != 31) {
                    number.append(".");
                }
            }
            i++;
        }

        String[] s1 = localhost.split("\\.");

        String[] s2 = number.toString().split("\\.");

        for (i = 0; i < s1.length; i++) {
            s1[i] = String.valueOf(Integer.valueOf(s1[i]) & Integer.valueOf(s2[i], 2));
        }
        s1[s1.length - 1] = "1";
        return StringUtils.join(s1, ".");
    }

    public static String geRackhdUrlById(String id) {
        Endpoint endpoint = staticEndpointMapper.selectByPrimaryKey(id);

        if ("rackshift-proxy".equalsIgnoreCase(endpoint.getIp())) {
            try {
                endpoint.setIp(getGateWayAddress(""));
            } catch (Exception unknownHostException) {
                unknownHostException.printStackTrace();
            }
        }

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
        try {
            String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.WORKFLOWS, null);
            if (StringUtils.isNotBlank(res))
                return JSONArray.parseArray(res);
        } catch (Exception e) {
        }
        return new JSONArray();
    }

    @Bean
    public JSONArray allTask() {
        try {
            String res = HttpFutureUtils.getHttp(getRackhdUrl() + RackHDConstants.TASKS, null);
            if (StringUtils.isNotBlank(res))
                return JSONArray.parseArray(res);
            return JSONArray.parseArray(res);
        } catch (Exception e) {
        }
        return new JSONArray();
    }
}
