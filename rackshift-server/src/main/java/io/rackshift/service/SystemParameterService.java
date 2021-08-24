package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.SystemParameterDTO;
import io.rackshift.mybatis.domain.SystemParameter;
import io.rackshift.mybatis.domain.SystemParameterExample;
import io.rackshift.mybatis.mapper.SystemParameterMapper;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SystemParameterService {
    @Resource
    private SystemParameterMapper systemParameterMapper;

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
        SystemParameterExample e = new SystemParameterExample();
        if (StringUtils.isNotBlank(queryVO.getParamKey())) {
            e.createCriteria().andParamKeyLike(queryVO.getParamKey());
        }
        return e;
    }
}
