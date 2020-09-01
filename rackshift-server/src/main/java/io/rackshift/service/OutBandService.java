package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.OutBandDTO;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.strategy.statemachine.LifeEvent;
import io.rackshift.strategy.statemachine.LifeEventType;
import io.rackshift.strategy.statemachine.StateMachine;
import io.rackshift.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OutBandService {
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private BareMetalManager bareMetalManager;
    @Resource
    private StateMachine stateMachine;
    @Resource
    private RackHDService rackHDService;

    public void saveOrUpdate(OutBand o) {
        if (o.getId() != null) {
            OutBand dbOutBand = outBandMapper.selectByPrimaryKey(o.getId());
            o.setId(dbOutBand.getId());
            BeanUtils.copyBean(dbOutBand, o);
            outBandMapper.updateByPrimaryKeySelective(dbOutBand);
        }
        OutBandExample example = new OutBandExample();
        example.createCriteria().andIpEqualTo(o.getIp());
        if (outBandMapper.countByExample(example) == 0) {
            outBandMapper.insertSelective(o);
        } else {
            OutBand dbOutBand = outBandMapper.selectByExample(example).get(0);
            o.setId(dbOutBand.getId());
            BeanUtils.copyBean(dbOutBand, o);
            outBandMapper.updateByPrimaryKeySelective(dbOutBand);
        }
    }

    public void fillOBMS(String bareMetalId, OutBand outBand) {
        JSONObject params = new JSONObject();
        params.put("outband", outBand);
        LifeEvent input = LifeEvent.builder().withBareMetalId(bareMetalId).withEventType(LifeEventType.FILL_OBMS).withParams(params);
        stateMachine.sendEvent(input);
    }

    public Object add(OutBandDTO queryVO) {
        OutBand image = new OutBand();
        BeanUtils.copyBean(image, queryVO);
        BareMetal bareMetal = bareMetalManager.getBareMetalByIp(queryVO.getIp());
        if (bareMetal == null) {
            return null;
        }
        outBandMapper.insertSelective(image);
        rackHDService.createOrUpdateObm(queryVO, bareMetal);
        return true;
    }

    public Object update(OutBandDTO queryVO) {
        OutBand image = new OutBand();
        BeanUtils.copyBean(image, queryVO);
        outBandMapper.updateByPrimaryKeySelective(image);

        BareMetal bareMetal = bareMetalManager.getBareMetalByIp(queryVO.getIp());
        if (bareMetal == null) {
            return null;
        }
        rackHDService.createOrUpdateObm(queryVO, bareMetal);
        return true;
    }

    public Object del(String id) {
        outBandMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<OutBand> list(OutBandDTO queryVO) {
        OutBandExample example = buildExample(queryVO);
        return outBandMapper.selectByExample(example);
    }

    private OutBandExample buildExample(OutBandDTO queryVO) {
        return new OutBandExample();
    }
}
