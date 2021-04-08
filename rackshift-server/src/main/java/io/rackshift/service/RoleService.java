package io.rackshift.service;

import io.rackshift.model.RSException;
import io.rackshift.model.RoleDTO;
import io.rackshift.mybatis.domain.Role;
import io.rackshift.mybatis.mapper.RoleMapper;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RoleService {
    @Resource
    RoleMapper roleMapper;

    public List<Role> list(RoleDTO queryVO) {
        return roleMapper.selectByExample(null);
    }

    public boolean update(RoleDTO queryVO) {
        if (StringUtils.isBlank(queryVO.getId())) {
            RSException.throwExceptions(Translator.get("error"));
        }
        roleMapper.updateByPrimaryKey(queryVO);
        return true;
    }

    public boolean del(String id) {
        if (StringUtils.isBlank(id)) {
            RSException.throwExceptions(Translator.get("error"));
        }
        roleMapper.deleteByPrimaryKey(id);
        return true;
    }

    public boolean add(RoleDTO queryVO) {
        queryVO.setId(UUID.randomUUID().toString());
        roleMapper.insertSelective(queryVO);
        return true;
    }

    public boolean del(String[] ids) {
        if (ids == null) {
            RSException.throwExceptions(Translator.get("error"));
        }
        Arrays.stream(ids).filter(id -> StringUtils.isNotBlank(id)).collect(Collectors.toList()).toArray(ids);
        for (String id : ids) {
            del(id);
        }
        return true;
    }
}
