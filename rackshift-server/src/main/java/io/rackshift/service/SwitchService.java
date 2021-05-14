package io.rackshift.service;

import io.rackshift.job.model.ProtocolRequest;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.metal.sdk.util.SnmpWorker;
import io.rackshift.model.*;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class SwitchService {
    @Resource
    private SwitchMapper switchMapper;
    @Resource
    private SwitchPortMapper switchPortMapper;

    public List<Switch> list(SwitchQueryVO queryVO) {
        SwitchExample e = buildExample(queryVO);
        return switchMapper.selectByExample(e);
    }

    private SwitchExample buildExample(SwitchQueryVO queryVO) {
        SwitchExample e = new SwitchExample();
        if (StringUtils.isNotBlank(queryVO.getSearchKey())) {
            e.createCriteria().andNameLike("%" + queryVO.getSearchKey() + "%");
            e.or().andIpLike("%" + queryVO.getSearchKey() + "%");
        }
        return e;
    }

    public boolean del(String[] ids) {
        if (ids == null || ids.length == 0) {
            return false;
        }
        for (String id : ids) {
            switchMapper.deleteByPrimaryKey(id);
        }
        return true;
    }

    public Switch getMachineEntity(ProtocolRequest request, SwitchRule switchRule, SnmpWorker snmpWorker) throws IOException {
        Switch switchObj = new Switch();
        switchObj.setIp(request.getHost());
        switchObj.setVendor(getVendor(snmpWorker));
        switchObj.setName(getSwitchName(snmpWorker));
        switchObj.setRuleId(switchRule.getId());
        switchObj.setSnmpCommunity(request.getCommunity());
        switchObj.setSnmpPort(String.valueOf(request.getPort()));
        return switchObj;
    }

    private String getVendor(SnmpWorker snmpWorker) throws IOException {
        return snmpWorker.getAsString("1.3.6.1.2.1.1.1.0");
    }

    private String getSwitchName(SnmpWorker snmpWorker) throws IOException {
        return snmpWorker.getAsString("1.3.6.1.2.1.1.5.0");
    }


    public void saveOrUpdate(Switch switchDevice) {
        SwitchExample e = new SwitchExample();
        e.createCriteria().andIpEqualTo(switchDevice.getIp());
        switchDevice.setUpdateTime(System.currentTimeMillis());
        List<Switch> switches = switchMapper.selectByExample(e);
        if (switches.size() == 0) {
            switchDevice.setId(UUIDUtil.newUUID());
            switchDevice.setCreateTime(System.currentTimeMillis());
            switchMapper.insertSelective(switchDevice);
        } else {
            switches.forEach(s -> {
                switchDevice.setId(s.getId());
                switchMapper.updateByPrimaryKeySelective(switchDevice);
            });
        }
    }


    public void saveOrUpdatePorts(List<SwitchPort> ports) {
        if (ports == null || ports.size() == 0)
            return;
        SwitchPortExample e = new SwitchPortExample();
        e.createCriteria().andSwitchIdEqualTo(ports.get(0).getSwitchId());
        switchPortMapper.deleteByExample(e);
        ports.forEach(p -> {
            p.setId(UUIDUtil.newUUID());
            p.setCreateTime(System.currentTimeMillis());
            p.setUpdateTime(System.currentTimeMillis());
            switchPortMapper.insertSelective(p);
        });
    }
}
