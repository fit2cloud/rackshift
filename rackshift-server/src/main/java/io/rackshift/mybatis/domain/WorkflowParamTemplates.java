package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class WorkflowParamTemplates implements Serializable {
    private String id;

    private String userId;

    private String bareMetalId;

    private String workflowName;

    private String workflowId;

    private static final long serialVersionUID = 1L;
}