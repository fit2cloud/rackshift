package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Task implements Serializable {
    private String id;

    private String preTaskId;

    private String workFlowId;

    private String bareMetalId;

    private String userId;

    private String instanceId;

    private String status;

    private Long createTime;

    private Long updateTime;

    private String beforeStatus;

    private static final long serialVersionUID = 1L;
}