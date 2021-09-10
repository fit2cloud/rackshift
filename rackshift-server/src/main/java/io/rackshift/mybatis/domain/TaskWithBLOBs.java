package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskWithBLOBs extends Task implements Serializable {
    private String params;

    private String extparams;

    private String graphObjects;

    private static final long serialVersionUID = 1L;
}