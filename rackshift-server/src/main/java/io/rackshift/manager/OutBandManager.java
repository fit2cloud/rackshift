package io.rackshift.manager;

import io.rackshift.mybatis.domain.OutBand;
import io.rackshift.mybatis.domain.OutBandExample;
import io.rackshift.mybatis.mapper.OutBandMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class OutBandManager {
    @Resource
    private OutBandMapper outBandMapper;

    public List<OutBand> getByBareMetalId(String id) {
        OutBandExample example = new OutBandExample();
        example.createCriteria().andBareMetalIdEqualTo(id);
        return outBandMapper.selectByExample(example);
    }

    public void save(OutBand outBand) {
        outBandMapper.insertSelective(outBand);
    }

    public void deleteById(String id) {
        OutBandExample e = new OutBandExample();
        e.createCriteria().andBareMetalIdEqualTo(id);
        outBandMapper.deleteByExample(e);
    }
}
