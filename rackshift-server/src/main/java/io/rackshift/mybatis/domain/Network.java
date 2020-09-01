package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Network implements Serializable {
    private String id;

    private String endpointId;

    private String name;

    private String vlanId;

    private String startIp;

    private String endIp;

    private String netmask;

    private Boolean dhcpEnable;

    private Boolean pxeEnable;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}