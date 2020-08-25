package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Workflow implements Serializable {
    private String id;

    private String injectableName;

    private String friendlyName;

    private String eventType;

    private String brands;

    private String settable;

    private String status;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}