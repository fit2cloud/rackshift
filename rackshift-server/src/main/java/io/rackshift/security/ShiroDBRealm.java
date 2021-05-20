package io.rackshift.security;

import io.rackshift.constants.AuthorizationConstants;
import io.rackshift.model.RSException;
import io.rackshift.model.UserDTO;
import io.rackshift.mybatis.mapper.UserMapper;
import io.rackshift.service.UserService;
import io.rackshift.utils.SsoSessionHandler;
import io.rackshift.utils.Translator;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import javax.annotation.Resource;
import java.util.HashSet;
import java.util.Set;

public class ShiroDBRealm extends AuthorizingRealm {

    private Logger logger = LoggerFactory.getLogger(ShiroDBRealm.class);
    @Resource
    private UserMapper userMapper;
    @Resource
    private UserService userService;

    @Value("${run.mode:release}")
    private String runMode;

    private Set<String> roles = new HashSet<String>() {{
        add(AuthorizationConstants.ROLE_ADMIN);
    }};

    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        String userName = (String) principalCollection.getPrimaryPrincipal();
        SimpleAuthorizationInfo simpleAuthenticationInfo = new SimpleAuthorizationInfo();
        UserDTO userDTO = userService.getUserDTO(userName);
        if (userDTO == null) {
            userDTO = userService.getUserDTOByEmail(userName);
            if (userDTO == null) {
                RSException.throwExceptions(Translator.get("error"));
            }
        }
//        Set<String> roles = userDTO.getRoles().stream().map(Role::getType).collect(Collectors.toSet());
        //暂时不使用 RBAC 默认所有添加的用户都是 管理员

        simpleAuthenticationInfo.setRoles(roles);

        return simpleAuthenticationInfo;
    }

    @Override
    public boolean isPermitted(PrincipalCollection principals, String permission) {
        return true;
    }

    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken) throws AuthenticationException {
        String userName = (String) authenticationToken.getPrincipal();
        String password = String.valueOf(((UsernamePasswordToken) authenticationToken).getPassword());
        UserDTO user = userService.getUserDTO(userName);
        if (user == null) {
            user = userService.getUserDTOByEmail(userName);
            if (user == null) {
                RSException.throwExceptions("");
            }
        }
        if (!"local".equals(runMode)) {
            if (!password.equalsIgnoreCase(SsoSessionHandler.random) && !userService.checkPassword(userName, password)) {
                RSException.throwExceptions("password_wrong");
            }
        }

        SecurityUtils.getSubject().getSession().setAttribute("user", user);
        return new SimpleAuthenticationInfo(userName, password, getName());
    }
}
