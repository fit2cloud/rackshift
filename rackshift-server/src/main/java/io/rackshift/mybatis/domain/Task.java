package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

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