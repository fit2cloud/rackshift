package io.rackshift.service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import io.rackshift.job.SyncNetworkJob;
import io.rackshift.job.model.DHCPConfig;
import io.rackshift.metal.sdk.util.HttpFutureUtils;
import io.rackshift.model.NetworkDTO;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.Network;
import io.rackshift.mybatis.domain.NetworkExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.NetworkMapper;
import io.rackshift.mybatis.mapper.ext.ExtNetworkMapper;
import io.rackshift.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NetworkService {
    @Resource
    private NetworkMapper networkMapper;
    @Resource
    private ExtNetworkMapper extNetworkMapper;
    @Resource
    private EndpointMapper endpointMapper;
    @Resource
    private SyncNetworkJob syncNetworkJob;

    public Object add(NetworkDTO queryVO) {
        return update(queryVO);
    }

    private static final String contentTypeJSON = "application/json";

    public Object update(NetworkDTO queryVO) {
        Network network = new Network();
        BeanUtils.copyBean(network, queryVO);

        Endpoint endpoint = getEndpoint(network.getEndpointId());
        if (endpoint == null) return false;

        NetworkExample e = new NetworkExample();
        e.createCriteria().andEndpointIdEqualTo(queryVO.getEndpointId());
        List<Network> networks = networkMapper.selectByExample(e);
        Set<String> ipSet = new HashSet<>();

        for (Network n : networks) {
            int total = 0;
            ipSet = new HashSet<>();
            List<String> ipRanges = IpUtil.getIpRange(n.getStartIp(), n.getEndIp(), n.getStartIp(), n.getNetmask());
            List<String> ipRanges2 = IpUtil.getIpRange(queryVO.getStartIp(), queryVO.getEndIp(), queryVO.getStartIp(), queryVO.getNetmask());
            total = ipRanges.size() + ipRanges2.size();
            ipSet.addAll(ipRanges);
            ipSet.addAll(ipRanges2);
            if (total != ipSet.size()) {
                //说明有交集
                if (!n.getId().equalsIgnoreCase(queryVO.getId())) {
                    RSException.throwExceptions(Translator.get("ip_range_conflict"));
                }
            }
        }

        String requesetStr = buildRequestStr(network);

        String res = null;
        if (StringUtils.isBlank(queryVO.getId()))
            res = HttpFutureUtils.postHttps("http://" + endpoint.getIp() + ":8083/dhcp/addDHCPConfig", requesetStr, contentTypeJSON, ProxyUtil.getHeaders());
        else
            res = HttpFutureUtils.postHttps("http://" + endpoint.getIp() + ":8083/dhcp/saveDHCPConfig", requesetStr, contentTypeJSON, ProxyUtil.getHeaders());
//        String res = HttpFutureUtils.postHttps("http://" + endpoint.getIp() + ":8083/dhcp/addDHCPConfig", requesetStr, contentTypeJSON);
        if (StringUtils.isNotBlank(res)) {
            JSONObject resObj = JSONObject.parseObject(res);
            if (resObj.containsKey("success") && resObj.getBoolean("success")) {
                if (StringUtils.isNotBlank(network.getId()))
                    networkMapper.updateByPrimaryKeySelective(network);
                else
                    networkMapper.insertSelective(network);
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

        String res = HttpFutureUtils.postHttps("http://" + endpoint.getIp() + ":8083/dhcp/delDHCPConfig", requesetStr, contentTypeJSON, ProxyUtil.getHeaders());
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
        if (StringUtils.isBlank(network.getId())) {
            DHCPConfig config = getDhcpConfig(network);
            return JSONObject.toJSONString(config);
        } else {
            List<DHCPConfig> configs = new LinkedList<>();
            Network dbNetwork = networkMapper.selectByPrimaryKey(network.getId());
            if (dbNetwork == null) return "";
            //顺序不能换
            configs.add(getDhcpConfig(dbNetwork));
            configs.add(getDhcpConfig(network));
            return JSONArray.toJSONString(configs);
        }
    }

    private DHCPConfig getDhcpConfig(Network network) {
        DHCPConfig config = new DHCPConfig();
        config.setStartIp(network.getStartIp());
        config.setEndIp(network.getEndIp());
        config.setNetmask(network.getNetmask());
        config.setPxeEnabled(Optional.ofNullable(network.getPxeEnable()).orElse(Boolean.FALSE));
        return config;
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

    public Map list(NetworkDTO queryVO, int page, int pageSize) {
        Map r = new HashMap();
        syncNetworkJob.run();
        Page<Object> page1 = PageHelper.startPage(page, pageSize, true);
        NetworkExample example = buildExample(queryVO);
        r.put("list", networkMapper.selectByExample(example));
        r.put("page", page1);
        return r;
    }

    private NetworkExample buildExample(NetworkDTO queryVO) {
        return new NetworkExample();
    }

    public void saveOrUpdate(List<Network> nList) {
        NetworkExample e = new NetworkExample();
        Network n = nList.get(0);
        e.createCriteria().andEndpointIdEqualTo(n.getEndpointId());
        Set<String> oldIds = networkMapper.selectByExample(e).stream().map(Network::getId).collect(Collectors.toSet());

        for (Network network : nList) {
            List<Network> networks = extNetworkMapper.getByType(network);
            if (networks.size() == 0) {
                networkMapper.insertSelective(network);
            } else {
                networks.forEach(n1 -> {
                    n1.setPxeEnable(network.getPxeEnable());
                    n1.setDhcpEnable(network.getPxeEnable());
                    n1.setStartIp(network.getStartIp());
                    n1.setEndIp(network.getEndIp());
                    n1.setNetmask(network.getNetmask());
                    n1.setName(network.getName());
                    n1.setEndpointId(network.getEndpointId());
                    networkMapper.updateByPrimaryKeySelective(n1);
                    oldIds.remove(n1.getId());
                });
            }
        }

        e.clear();
        List l = new ArrayList<>();
        l.addAll(oldIds);
        if (oldIds.size() > 0) {
            e.createCriteria().andIdIn(l);
            networkMapper.deleteByExample(e);
        }
    }

    public void clear(String k) {
        NetworkExample e = new NetworkExample();
        e.createCriteria().andEndpointIdEqualTo(k);
        networkMapper.deleteByExample(e);
    }

}