package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class DiscoveryDevices implements Serializable {
    private String id;

    private String bareMetalRuleId;

    private String name;

    private String ip;

    private String description;

    private Long createTime;

    private Long updateTime;

    private static final long serialVersionUID = 1L;
}