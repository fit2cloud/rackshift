package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowParamTemplatesWithBLOBs extends WorkflowParamTemplates implements Serializable {
    private String paramsTemplate;

    private String extraParams;

    private static final long serialVersionUID = 1L;
}