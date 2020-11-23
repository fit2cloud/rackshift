package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class ExecutionLogDetails implements Serializable {
    private String id;

    private String user;

    private String operation;

    private String logId;

    private String bareMetalId;

    private String status;

    private Long createTime;

    private String instanceId;

    private String outPut;

    private static final long serialVersionUID = 1L;
}