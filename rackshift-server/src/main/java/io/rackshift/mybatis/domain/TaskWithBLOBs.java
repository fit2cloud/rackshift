package io.rackshift.mybatis.domain;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import java.io.Serializable;

@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class TaskWithBLOBs extends Task implements Serializable {
    private String params;

    private String extparams;

    private static final long serialVersionUID = 1L;
}