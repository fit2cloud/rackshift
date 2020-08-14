package io.rackshift.service;

import io.rackshift.model.SystemParameterDTO;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.domain.SystemParameterExample;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.utils.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class SystemParameterService {
    @Resource
    private SystemParameterMapper systemParameterMapper;
    @Resource
    private ApplicationContext applicationContext;

    public Object add(SystemParameterDTO queryVO) {
        SystemParameter image = new SystemParameter();
        BeanUtils.copyBean(image, queryVO);
        systemParameterMapper.insertSelective(image);
        return true;
    }

    public Object update(SystemParameterDTO queryVO) {
        SystemParameter image = new SystemParameter();
        BeanUtils.copyBean(image, queryVO);
        systemParameterMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public Object del(String id) {
        systemParameterMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<SystemParameter> list(SystemParameterDTO queryVO) {
        SystemParameterExample example = buildExample(queryVO);
        return systemParameterMapper.selectByExample(example);
    }

    private SystemParameterExample buildExample(SystemParameterDTO queryVO) {
        return new SystemParameterExample();
    }
}
