package io.rackshift.service;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.*;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.strategy.ipmihandler.base.IPMIHandlerDecorator;
import io.rackshift.strategy.statemachine.LifeStatus;
import io.rackshift.utils.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SwitchService {
    @Resource
    private SwitchMapper switchMapper;

    public List<Switch> list(SwitchQueryVO queryVO) {
        SwitchExample e = buildExample(queryVO);
        return switchMapper.selectByExample(e);
    }

    private SwitchExample buildExample(SwitchQueryVO queryVO) {
        return new SwitchExample();
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
}
