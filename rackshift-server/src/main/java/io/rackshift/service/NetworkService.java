package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.job.model.DHCPConfig;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.model.NetworkDTO;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.Network;
import io.rackshift.mybatis.domain.NetworkExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.NetworkMapper;
import io.rackshift.mybatis.mapper.ext.ExtNetworkMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.LogUtil;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NetworkService {
    @Resource
    private NetworkMapper networkMapper;
    @Resource
    private ExtNetworkMapper extNetworkMapper;
    @Resource
    private EndpointMapper endpointMapper;

    public Object add(NetworkDTO queryVO) {
        return update(queryVO);
    }

    public Object update(NetworkDTO queryVO) {
        Network network = new Network();
        BeanUtils.copyBean(network, queryVO);

        Endpoint endpoint = getEndpoint(network.getEndpointId());
        if (endpoint == null) return false;
        String requesetStr = buildRequestStr(network);

        String res = HttpFutureUtils.postHttps("http://localhost:8083/dhcp/addDHCPConfig", requesetStr, null);
//        String res = HttpFutureUtils.postHttps("http://" + endpoint.getIp() + ":8083/dhcp/addDHCPConfig", requesetStr, null);
        if (StringUtils.isNotBlank(res)) {
            JSONObject resObj = JSONObject.parseObject(res);
            if (resObj.containsKey("success") && resObj.getBoolean("success")) {
                networkMapper.updateByPrimaryKeySelective(network);
                return true;
            } else {
                LogUtil.error(Translator.get("update_dhcp_config_fail"));
                return false;
            }
        }
        return false;
    }

    public Object del(String id) {

        Network network = networkMapper.selectByPrimaryKey(id);
        if (network == null) return false;
        Endpoint endpoint = getEndpoint(network.getEndpointId());
        if (endpoint == null) return false;
        String requesetStr = buildRequestStr(network);

        String res = HttpFutureUtils.postHttps("http://localhost" + ":8083/dhcp/delDHCPConfig", requesetStr, null);
        if (StringUtils.isNotBlank(res)) {
            JSONObject resObj = JSONObject.parseObject(res);
            if (resObj.containsKey("success") && resObj.getBoolean("success")) {
                networkMapper.deleteByPrimaryKey(id);
                return true;
            } else {
                LogUtil.error(Translator.get("delete_dhcp_config_fail"));
                return false;
            }
        }
        return false;
    }

    private String buildRequestStr(Network network) {
        DHCPConfig config = new DHCPConfig();
        config.setStartIp(network.getStartIp());
        config.setEndIp(network.getEndIp());
        config.setNetmask(network.getNetmask());
        config.setPxeEnabled(network.getPxeEnable());
        return JSONObject.toJSONString(config);
    }

    private Endpoint getEndpoint(String endpointId) {
        return endpointMapper.selectByPrimaryKey(endpointId);
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Network> list(NetworkDTO queryVO) {
        NetworkExample example = buildExample(queryVO);
        return networkMapper.selectByExample(example);
    }

    private NetworkExample buildExample(NetworkDTO queryVO) {
        return new NetworkExample();
    }

    public void saveOrUpdate(Network n) {
        List<Network> networks = extNetworkMapper.getByType(n);
        if (networks.size() == 0) {
            networkMapper.insertSelective(n);
        } else {
            networks.forEach(n1 -> {
                n1.setPxeEnable(n.getPxeEnable());
                n1.setDhcpEnable(n.getPxeEnable());
                n1.setStartIp(n.getStartIp());
                n1.setEndIp(n.getEndIp());
                n1.setNetmask(n.getNetmask());
                n1.setName(n.getName());
                n1.setEndpointId(n.getEndpointId());
                networkMapper.updateByPrimaryKeySelective(n1);
            });
        }
    }
}
