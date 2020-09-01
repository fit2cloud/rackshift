package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.SystemParameterDTO;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.domain.SystemParameterExample;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemParameterService {
    @Resource
    private SystemParameterMapper systemParameterMapper;
    @Resource
    private ApplicationContext applicationContext;

    public Object add(SystemParameterDTO queryVO) {

        SystemParameterExample e = buildExample(queryVO);
        if (systemParameterMapper.selectByExample(e).stream().count() > 0) {
            return false;
        }
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
        if (StringUtils.isNotBlank(queryVO.getParamKey()) && queryVO.getParamKey().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.value())) {
            SystemParameterExample e = new SystemParameterExample();
            e.createCriteria().andParamKeyEqualTo(queryVO.getParamKey());
            return e;
        }
        return new SystemParameterExample();
    }

    public Object getAllEndPointType() {
        return Arrays.asList(ServiceConstants.EndPointType.values()).stream().map(s -> JSONObject.parse(s.toString())).collect(Collectors.toList());
    }
}
