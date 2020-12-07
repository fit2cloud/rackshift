package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class NetworkCard implements Serializable {
    private String id;

    private String vlanId;

    private String ip;

    private String number;

    private String bareMetalId;

    private String mac;

    private Long syncTime;

    private static final long serialVersionUID = 1L;
}