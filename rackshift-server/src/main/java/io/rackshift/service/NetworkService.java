package io.rackshift.service;

import io.rackshift.model.NetworkDTO;
import io.rackshift.mybatis.domain.Network;
import io.rackshift.mybatis.domain.NetworkExample;
import io.rackshift.mybatis.mapper.NetworkMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class NetworkService {
    @Resource
    private NetworkMapper networkMapper;

    public Object add(NetworkDTO queryVO) {
        Network image = new Network();
        BeanUtils.copyBean(image, queryVO);
        networkMapper.insertSelective(image);
        return true;
    }

    public Object update(NetworkDTO queryVO) {
        Network image = new Network();
        BeanUtils.copyBean(image, queryVO);
        networkMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public Object del(String id) {
        networkMapper.deleteByPrimaryKey(id);
        return true;
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
}
