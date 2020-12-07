package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class WorkflowParamTemplates implements Serializable {
    private String id;

    private String userId;

    private String bareMetalId;

    private String workflowName;

    private static final long serialVersionUID = 1L;
}