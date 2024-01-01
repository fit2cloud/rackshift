package io.rackshift.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.model.DHCPConfig;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.Network;
import io.rackshift.service.NetworkService;
import io.rackshift.utils.ProxyUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class SyncNetworkJob {
    @Resource
    private NetworkService networkService;
    @Resource
    private WorkflowConfig workflowConfig;

    public void run(boolean full) {
        List<Network> networks = new LinkedList<>();
        for (Endpoint endPoint : workflowConfig.getEndPoints()) {
            if (!full) {
                if (!ServiceConstants.EndpointStatusEnum.Online.name().equalsIgnoreCase(endPoint.getStatus())) {
                    continue;
                }
            }
            String res = HttpFutureUtils.getHttp("http://" + endPoint.getIp() + ":8083/dhcp/configFile", ProxyUtil.getHeaders());
            if (StringUtils.isNotBlank(res)) {
                JSONObject obj = JSONObject.parseObject(res);
                if (obj.containsKey("data")) {
                    JSONArray dhcpconfigs = JSONObject.toJavaObject(obj.getJSONArray("data"), JSONArray.class);
                    networks.addAll(convert(dhcpconfigs, endPoint));
                }
            }
        }
        Map<String, List<Network>> endpointNetwork = networks.stream().collect(Collectors.groupingBy(Network::getEndpointId));
        endpointNetwork.keySet().forEach(endpointId -> {
            networkService.saveOrUpdate(endpointNetwork.get(endpointId));
        });

    }

    private Collection<? extends Network> convert(JSONArray dhcpconfigs, Endpoint endPoint) {
        List<Network> networks = new LinkedList<>();

        dhcpconfigs.forEach(c -> {
            DHCPConfig c1 = JSONObject.parseObject(c.toString(), DHCPConfig.class);
            Network network = new Network();
            network.setInterFace(c1.getinterFace());
            network.setStartIp(c1.getStartIp());
            network.setEndIp(c1.getEndIp());
            network.setNetmask(c1.getNetmask());
            network.setEndpointId(endPoint.getId());
            network.setDhcpEnable(c1.isPxeEnabled());
            network.setPxeEnable(c1.isPxeEnabled());
            network.setName(endPoint.getName() + "_" + c1.getNetSegment());
            networks.add(network);
        });
        return networks;
    }


}
