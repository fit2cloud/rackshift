package io.rackshift.model;

import io.rackshift.mybatis.domain.Role;
import io.rackshift.mybatis.domain.UserRole;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class UserDTO {
    private String id;

    private String name;

    private String password;

    private String email;

    private String phone;

    private String status;

    private Long createTime;

    private Long updateTime;

    private String language;

    private String lastWorkspaceId;

    private String lastOrganizationId;

    private List<Role> roles = new ArrayList<>();

    private List<UserRole> userRoles = new ArrayList<>();

    private List<String> rolesIds = new ArrayList<>();

    private static final long serialVersionUID = 1L;

}
