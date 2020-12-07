package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Memory implements Serializable {
    private String id;

    private String bareMetalId;

    private String memCpuNum;

    private String memModNum;

    private String memModSize;

    private String memModType;

    private String memModFrequency;

    private String memModPartNum;

    private String memModMinVolt;

    private String sn;

    private Long syncTime;

    private Byte status;

    private static final long serialVersionUID = 1L;
}