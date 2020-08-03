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

    public List<OutBand> getByIp(String ip){
        OutBandExample example = new OutBandExample();
        example.createCriteria().andIpEqualTo(ip);
        return outBandMapper.selectByExample(example);
    }
}
