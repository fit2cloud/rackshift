package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.config.WorkflowConfig;
import io.rackshift.constants.ServiceConstants;
import io.rackshift.job.EndpointPoller;
import io.rackshift.job.SyncRackJob;
import io.rackshift.model.ProfileDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.*;
import io.rackshift.utils.BeanUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProfileService {
    @Resource
    private ProfileMapper profileMapper;

    public Object add(ProfileDTO queryVO) {
        Profile image = new Profile();
        BeanUtils.copyBean(image, queryVO);
        profileMapper.insertSelective(image);
        return true;
    }

    public Object update(Profile queryVO) {
        Profile image = new Profile();
        BeanUtils.copyBean(image, queryVO);
        profileMapper.updateByPrimaryKeySelective(image);
        return true;
    }

    public boolean del(String id) {
        if (profileMapper.selectByPrimaryKey(id) == null) {
            return false;
        }
        if (profileMapper.selectByPrimaryKey(id).getType().equalsIgnoreCase(ServiceConstants.TYPE_SYS)) {
            return false;
        }
        profileMapper.deleteByPrimaryKey(id);
        return true;
    }

    public Object del(String[] ids) {
        for (String id : ids) {
            if (ServiceConstants.TYPE_SYS.equalsIgnoreCase(profileMapper.selectByPrimaryKey(id).getType())) {
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

    public List<Profile> list(ProfileDTO queryVO) {
        ProfileExample example = buildExample(queryVO);
        return profileMapper.selectByExample(example);
    }

    private ProfileExample buildExample(ProfileDTO queryVO) {
        return new ProfileExample();
    }

    public Profile getById(String id) {
        if (StringUtils.isBlank(id)) {
            return null;
        }
        return profileMapper.selectByPrimaryKey(id);
    }
}
