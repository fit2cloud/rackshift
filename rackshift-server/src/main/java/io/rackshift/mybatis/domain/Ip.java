package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Ip implements Serializable {
    private String id;

    private String ip;

    private String mask;

    private String gateway;

    private String dns1;

    private String dns2;

    private String region;

    private String networkId;

    private String resourceType;

    private String resourceId;

    private String orderItemId;

    private String status;

    private static final long serialVersionUID = 1L;
}