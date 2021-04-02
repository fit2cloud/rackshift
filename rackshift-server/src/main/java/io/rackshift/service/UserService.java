package io.rackshift.service;

import com.alibaba.fastjson.JSONObject;
import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.constants.UserStatus;
import io.rackshift.model.RSException;
import io.rackshift.model.UserDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.RoleMapper;
import io.rackshift.mybatis.mapper.UserMapper;
import io.rackshift.mybatis.mapper.UserRoleMapper;
import io.rackshift.mybatis.mapper.ext.ExtUserMapper;
import io.rackshift.utils.CodingUtil;
import io.rackshift.utils.SessionUtil;
import io.rackshift.utils.Translator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private ExtUserMapper extUserMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserDTO getUserDTO(String userName) {
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return null;
        }
        if (StringUtils.equals(UserStatus.DISABLED, user.getStatus())) {
            RSException.throwExceptions(Translator.get("user_has_bean_deleted!"));
        }
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(userName);
        List<UserRole> userRoleList = userRoleMapper.selectByExample(userRoleExample);
        UserDTO userDTO = new UserDTO();
        BeanUtils.copyProperties(user, userDTO);
        userDTO.setUserRoles(userRoleList);

        if (userRoleList.size() > 0) {
            RoleExample roleExample = new RoleExample();
            roleExample.createCriteria().andIdIn(userRoleList.stream().map(UserRole::getRoleId).collect(Collectors.toList()));
            List<Role> roles = roleMapper.selectByExample(roleExample);
            userDTO.setRoles(roles);
        }

        return userDTO;
    }

    public UserDTO getUserDTOByEmail(String email) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEmailEqualTo(email);
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() != 1) {
            return null;
        }
        return getUserDTO(users.get(0).getId());
    }

    public boolean checkPassword(String userName, String password) {
        UserExample userExample = new UserExample();
        userExample.createCriteria().andIdEqualTo(userName).andPasswordEqualTo(CodingUtil.md5(password));
        return userMapper.countByExample(userExample) > 0;
    }

    public List<UserDTO> list(UserDTO queryVO) {
        Map params = buidlExample(queryVO);
        return extUserMapper.list(params);
    }

    private Map buidlExample(UserDTO queryVO) {
        Map params = new HashMap<String, Object>();
        if (StringUtils.isNotBlank(queryVO.getId())) {
            params.put("id", queryVO.getId());
        }
        if (StringUtils.isNotBlank(queryVO.getEmail())) {
            params.put("id", queryVO.getEmail());
        }
        if (StringUtils.isNotBlank(queryVO.getPhone())) {
            params.put("id", queryVO.getPhone());
        }
        return params;
    }

    public boolean add(UserDTO queryVO) {
        try {
            User user = new User();
            BeanUtils.copyProperties(queryVO, user);
            user.setPassword(CodingUtil.md5(user.getPassword()));
            user.setStatus(UserStatus.NORMAL);
            userMapper.insertSelective(user);

            for (String roleId : queryVO.getRolesIds()) {
                UserRole userRole = new UserRole();
                userRole.setId(UUID.randomUUID().toString());
                userRole.setRoleId(roleId);
                userRole.setUserId(queryVO.getId());
                userRoleMapper.insertSelective(userRole);
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean update(UserDTO queryVO) {
        User user = new User();
        BeanUtils.copyProperties(queryVO, user);
        user.setPassword(null);
        userMapper.updateByPrimaryKeySelective(user);
        UserRoleExample userRoleExample = new UserRoleExample();
        userRoleExample.createCriteria().andUserIdEqualTo(queryVO.getId());
        userRoleMapper.deleteByExample(userRoleExample);

        for (String roleId : queryVO.getRolesIds()) {
            UserRole userRole = new UserRole();
            userRole.setId(UUID.randomUUID().toString());
            userRole.setRoleId(roleId);
            userRole.setUserId(queryVO.getId());
            userRoleMapper.insertSelective(userRole);
        }
        return true;
    }

    public boolean del(String id) {
        if (id.equalsIgnoreCase("admin")) {
            return false;
        }
        return userMapper.deleteByPrimaryKey(id) > 0;
    }

    public boolean del(String[] ids) {
        for (String id : ids) {
            if (AuthorizationConstants.ROLE_ADMIN.equalsIgnoreCase(id)) {
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

    public boolean change(JSONObject editObj) {
        String originPwd = editObj.getString("originPwd");
        String newPwd = editObj.getString("newPwd");
        String confirmPwd = editObj.getString("confirmPwd");
        if (StringUtils.isAnyBlank(originPwd, newPwd, confirmPwd)) {
            return false;
        }
        User user = userMapper.selectByPrimaryKey(SessionUtil.getUser().getId());

        if (CodingUtil.md5(originPwd).equalsIgnoreCase(user.getPassword())) {
            if (newPwd.equals(confirmPwd)) {

                user.setPassword(CodingUtil.md5(newPwd));
                userMapper.updateByPrimaryKey(user);
                return true;
            }
        }
        return false;
    }
}
