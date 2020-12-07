package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class OperationLog implements Serializable {
    private String id;

    private String workspaceId;

    private String workspaceName;

    private String resourceUserId;

    private String resourceUserName;

    private String resourceType;

    private String resourceId;

    private String resourceName;

    private String operation;

    private Long time;

    private String module;

    private String sourceIp;

    private String message;

    private static final long serialVersionUID = 1L;
}