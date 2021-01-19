package io.rackshift.service;

import io.rackshift.manager.BareMetalManager;
import io.rackshift.model.OutBandDTO;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.BareMetal;
import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.OutBandMapper;
import io.rackshift.utils.BeanUtils;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

@Service
public class OutBandService {
    @Resource
    private OutBandMapper outBandMapper;
    @Resource
    private BareMetalManager bareMetalManager;
    @Autowired
    private SimpMessagingTemplate template;
    @Resource
    private RackHDService rackHDService;
    @Resource
    private OutBandService outBandService;

    public void saveOrUpdate(OutBand o) {
        if (o.getId() != null) {
            OutBand dbOutBand = outBandMapper.selectByPrimaryKey(o.getId());
            o.setId(dbOutBand.getId());
            BeanUtils.copyBean(dbOutBand, o);
            outBandMapper.updateByPrimaryKeySelective(dbOutBand);
            return;
        }
        OutBandExample example = new OutBandExample();
        example.createCriteria().andIpEqualTo(o.getIp());
        outBandMapper.deleteByExample(example);
        outBandMapper.insertSelective(o);
    }

    public void fillOBMS(String bareMetalId, OutBand outBand) {
        BareMetal bareMetal = bareMetalManager.getBareMetalById(bareMetalId);
        rackHDService.createOrUpdateObm(outBand, bareMetal);
        outBandService.saveOrUpdate(outBand);
    }

    public Object add(OutBandDTO queryVO) {
        OutBand image = new OutBand();
        BeanUtils.copyBean(image, queryVO);
        BareMetal bareMetal = bareMetalManager.getBareMetalByIp(queryVO.getIp());
        if (bareMetal == null) {
            return null;
        }
        image.setBareMetalId(bareMetal.getId());
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

    public OutBand getByIp(String ip) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andIpEqualTo(ip);
        List<OutBand> outBands = outBandMapper.selectByExample(e);
        return outBands.size() > 0 ? outBands.get(0) : null;
    }

    public void fillOBMS(OutBandDTO outBand) {
        OutBand obm = outBand.getObm();
        if (StringUtils.isAnyBlank(obm.getUserName(), obm.getPwd())) {
            RSException.throwExceptions(Translator.get("i18n_error"));
            return;
        }
        for (String id : outBand.getIds()) {
            BareMetal bareMetal = bareMetalManager.getBareMetalById(id);
            if (bareMetal == null) {
                continue;
            }
            OutBandExample e = new OutBandExample();
            e.createCriteria().andBareMetalIdEqualTo(bareMetal.getId());
            List<OutBand> outBands = outBandMapper.selectByExample(e);
            if (outBands.size() > 0) {
                outBands.forEach(o -> {
                    o.setUserName(obm.getUserName());
                    o.setPwd(obm.getPwd());
                    fillOBMS(bareMetal.getId(), o);
                });
            } else {
                obm.setId(null);
                obm.setIp(bareMetal.getManagementIp());
                obm.setBareMetalId(id);
                fillOBMS(bareMetal.getId(), obm);
            }
        }
    }

    public List<OutBand> getByBareMetalIds(String[] bareMetalIds) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andBareMetalIdIn(Arrays.asList(bareMetalIds));
        return outBandMapper.selectByExample(e);
    }
}
