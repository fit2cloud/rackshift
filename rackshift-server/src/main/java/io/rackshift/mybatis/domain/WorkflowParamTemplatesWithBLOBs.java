package io.rackshift.mybatis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowParamTemplatesWithBLOBs extends WorkflowParamTemplates implements Serializable {
    private String paramsTemplate;

    private String extraParams;

    private static final long serialVersionUID = 1L;
}