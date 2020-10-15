package io.rackshift.service;

import io.rackshift.model.BareMetalDTO;
import io.rackshift.model.DiscoveryDevicesDTO;
import io.rackshift.mybatis.domain.DiscoveryDevices;
import io.rackshift.mybatis.domain.DiscoveryDevicesExample;
import io.rackshift.mybatis.mapper.DiscoveryDevicesMapper;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Optional;

@Service
public class DiscoveryDevicesService {
    @Resource
    private DiscoveryDevicesMapper discoveryDevicesMapper;
    @Resource
    private BareMetalService bareMetalService;
    @Resource
    private EndpointMapper endpointMapper;

    public Object add(DiscoveryDevicesDTO queryVO) {
        DiscoveryDevices DiscoveryDevices = new DiscoveryDevices();
        BeanUtils.copyBean(DiscoveryDevices, queryVO);
        discoveryDevicesMapper.insertSelective(DiscoveryDevices);
        return true;
    }

    public Object add(DiscoveryDevices queryVO) {
        discoveryDevicesMapper.insertSelective(queryVO);
        return true;
    }

    public DiscoveryDevices getByIp(String ip) {
        if (Optional.of(ip).isPresent()) {
            DiscoveryDevicesExample e = new DiscoveryDevicesExample();
            e.createCriteria().andIpEqualTo(ip);
            List<DiscoveryDevices> ddList = discoveryDevicesMapper.selectByExample(e);
            if (ddList.size() > 0) {
                return ddList.get(0);
            }
        }
        return null;
    }

    public Object update(DiscoveryDevices queryVO) {
        DiscoveryDevices DiscoveryDevices = new DiscoveryDevices();
        BeanUtils.copyBean(DiscoveryDevices, queryVO);
        discoveryDevicesMapper.updateByPrimaryKey(DiscoveryDevices);
        return true;
    }

    public Object del(String id) {
        DiscoveryDevices DiscoveryDevices = discoveryDevicesMapper.selectByPrimaryKey(id);
        if (DiscoveryDevices == null) return false;
        discoveryDevicesMapper.deleteByPrimaryKey(id);
        return false;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<DiscoveryDevices> list(DiscoveryDevicesDTO queryVO) {
        DiscoveryDevicesExample example = buildExample(queryVO);
        return discoveryDevicesMapper.selectByExample(example);
    }

    private DiscoveryDevicesExample buildExample(DiscoveryDevicesDTO queryVO) {
        return new DiscoveryDevicesExample();
    }

    public boolean addToBareMetal(BareMetalDTO bareMetalDTO) {
        return bareMetalService.addToBareMetal(bareMetalDTO);
    }
}
