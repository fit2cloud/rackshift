package io.rackshift.model;

import io.rackshift.mybatis.domain.Role;
import io.rackshift.mybatis.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserRoleDTO {

    private String userId;
    private List<Role> roles;
    private List<UserRole> userRoles;
    private static final long serialVersionUID = 1L;

}
