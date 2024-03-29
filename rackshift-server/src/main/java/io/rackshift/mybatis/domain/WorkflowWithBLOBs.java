package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class WorkflowWithBLOBs extends Workflow implements Serializable {
    private String defaultParams;

    private String tasks;

    private String options;

    private static final long serialVersionUID = 1L;
}