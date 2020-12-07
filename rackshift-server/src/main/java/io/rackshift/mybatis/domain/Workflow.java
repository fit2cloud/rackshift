package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Workflow implements Serializable {
    private String id;

    private String type;

    private String injectableName;

    private String friendlyName;

    private String eventType;

    private String brands;

    private String settable;

    private String status;

    private Long createTime;

    private String defaultParams;

    private static final long serialVersionUID = 1L;
}