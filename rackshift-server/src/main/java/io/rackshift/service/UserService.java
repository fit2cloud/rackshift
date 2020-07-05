package io.rackshift.service;

import io.rackshift.model.UserDTO;
import io.rackshift.mybatis.domain.*;
import io.rackshift.mybatis.mapper.RoleMapper;
import io.rackshift.mybatis.mapper.UserMapper;
import io.rackshift.mybatis.mapper.UserRoleMapper;
import io.rackshift.utils.CodingUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Resource
    private UserMapper userMapper;
    @Resource
    private RoleMapper roleMapper;
    @Resource
    private UserRoleMapper userRoleMapper;

    public UserDTO getUserDTO(String userName) {
        User user = userMapper.selectByPrimaryKey(userName);
        if (user == null) {
            return null;
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
}
