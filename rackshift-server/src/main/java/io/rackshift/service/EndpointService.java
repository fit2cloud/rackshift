package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.EndpointPoller;
import io.rackshift.job.SyncRackJob;
import io.rackshift.model.EndpointDTO;
import io.rackshift.model.RSException;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.BareMetalMapper;
import io.rackshift.mybatis.mapper.EndpointMapper;
import io.rackshift.mybatis.mapper.ImageMapper;
import io.rackshift.mybatis.mapper.NetworkMapper;
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
    private WorkflowConfig workflowConfig;
    @Resource
    private SyncRackJob syncRackJob;
    @Resource
    private EndpointPoller endpointPoller;
    @Resource
    private BareMetalMapper bareMetalMapper;
    @Resource
    private ImageMapper imageMapper;
    @Resource
    private NetworkMapper networkMapper;

    public Object add(EndpointDTO queryVO) {

        EndpointExample e = buildExample(queryVO);
        if (endpointMapper.selectByExample(e).stream().count() > 0) {
            return false;
        }
        e.clear();
        if (ServiceConstants.EndPointType.main_endpoint.name().equals(queryVO.getType())) {
            e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.name());
            e.or().andIpEqualTo(queryVO.getIp());
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

    public Object update(Endpoint queryVO) {
        Endpoint image = new Endpoint();
        BeanUtils.copyBean(image, queryVO);

        if (StringUtils.isNotBlank(queryVO.getIp())) {
            EndpointExample e = new EndpointExample();
            e.createCriteria().andIpEqualTo(queryVO.getIp()).andIdNotEqualTo(queryVO.getId());
            if (endpointMapper.selectByExample(e).stream().count() > 0) {
                return false;
            }
        }

        workflowConfig.initWorkflow();
        endpointMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public boolean del(String id) {
        if (endpointMapper.selectByPrimaryKey(id) == null) {
            return true;
        }
        if (endpointMapper.selectByPrimaryKey(id).getType().equalsIgnoreCase(ServiceConstants.EndPointType.main_endpoint.name())) {
            return false;
        }

        int i = endpointMapper.deleteByPrimaryKey(id);
        if (i == 1) {
            delRelated(id);
        }
        workflowConfig.initWorkflow();
        return true;
    }

    private void delRelated(String id) {
        BareMetalExample be = new BareMetalExample();
        be.createCriteria().andEndpointIdEqualTo(id);
        bareMetalMapper.deleteByExample(be);

        NetworkExample ne = new NetworkExample();
        ne.createCriteria().andEndpointIdEqualTo(id);
        networkMapper.deleteByExample(ne);

        ImageExample ie = new ImageExample();
        ie.createCriteria().andEndpointIdEqualTo(id);
        imageMapper.deleteByExample(ie);
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            if (ServiceConstants.EndPointType.main_endpoint.name().equalsIgnoreCase(endpointMapper.selectByPrimaryKey(id).getType())) {
                return false;
            }
        }
        for (String id : ids) {
            if (!del(id)) {
                return false;
            }
        }
        return true;
    }

    public List<Endpoint> list(EndpointDTO queryVO) {
        EndpointExample example = buildExample(queryVO);
        return endpointMapper.selectByExample(example);
    }

    private EndpointExample buildExample(EndpointDTO queryVO) {
        if (StringUtils.isNotBlank(queryVO.getIp())) {
            EndpointExample e = new EndpointExample();
            e.createCriteria().andIpEqualTo(queryVO.getIp());
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

    public Endpoint getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return endpointMapper.selectByPrimaryKey(id);
    }

    public boolean sync() {
        return syncRackJob.run() && endpointPoller.run();
    }

    public Endpoint getMainEndpoint() {
        EndpointExample e = new EndpointExample();
        e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.value());
        if (endpointMapper.selectByExample(e).size() == 0)
            RSException.throwExceptions("no main endpoint !");
        return endpointMapper.selectByExample(e).get(0);
    }

    public String getMainEndpointIp() {
        EndpointExample e = new EndpointExample();
        e.createCriteria().andTypeEqualTo(ServiceConstants.EndPointType.main_endpoint.value());
        if (endpointMapper.selectByExample(e).size() == 0)
            RSException.throwExceptions("no main endpoint !");
        return endpointMapper.selectByExample(e).get(0).getIp();
    }
}
