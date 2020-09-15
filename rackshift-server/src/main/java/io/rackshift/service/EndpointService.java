package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.model.EndpointDTO;
import io.rackshift.mybatis.domain.Endpoint;
import io.rackshift.mybatis.domain.EndpointExample;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EndpointService {
    @Resource
    private EndpointMapper endpointMapper;
    @Resource
    private ApplicationContext applicationContext;
    @Resource
    private WorkflowConfig workflowConfig;
    public Object add(EndpointDTO queryVO) {

        EndpointExample e = buildExample(queryVO);
        if (endpointMapper.selectByExample(e).stream().count() > 0) {

            return false;
        }
        e.clear();
        if (ServiceConstants.EndPointType.main_endpoint.name().equals(queryVO.getType())) {
            e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.name());
            if (endpointMapper.selectByExample(e).stream().count() > 0) {
                return false;
            }
        }
        Endpoint image = new Endpoint();
        BeanUtils.copyBean(image, queryVO);
        endpointMapper.insertSelective(image);
        workflowConfig.initWorkflow();
        return true;
    }

    public Object update(EndpointDTO queryVO) {
        Endpoint image = new Endpoint();
        BeanUtils.copyBean(image, queryVO);
        workflowConfig.initWorkflow();
        endpointMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public Object del(String id) {
        if (endpointMapper.selectByPrimaryKey(id) == null) {
            return true;
        }
        if (endpointMapper.selectByPrimaryKey(id).getType().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.name())) {
            return false;
        }

        endpointMapper.deleteByPrimaryKey(id);
        workflowConfig.initWorkflow();
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            del(id);
        }
        return null;
    }

    public List<Endpoint> list(EndpointDTO queryVO) {
        EndpointExample example = buildExample(queryVO);
        return endpointMapper.selectByExample(example);
    }

    private EndpointExample buildExample(EndpointDTO queryVO) {
        if (StringUtils.isNotBlank(queryVO.getName())) {
            EndpointExample e = new EndpointExample();
            e.createCriteria().andNameEqualTo(queryVO.getName());
            return e;
        }
        return new EndpointExample();
    }

    public Object getAllEndPointType() {
        return Arrays.asList(ServiceConstants.EndPointType.values()).stream().map(s -> JSONObject.parse(s.toString())).collect(Collectors.toList());
    }

    public List<Endpoint> getAllEndPoints() {
        return endpointMapper.selectByExample(new EndpointExample());
    }
}
