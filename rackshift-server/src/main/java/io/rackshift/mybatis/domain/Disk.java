package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Disk implements Serializable {
    private String id;

    private String bareMetalId;

    private Integer enclosureId;

    private Integer controllerId;

    private String drive;

    private String type;

    private String size;

    private String raid;

    private String virtualDisk;

    private String manufactor;

    private Long syncTime;

    private String sn;

    private String model;

    private Byte status;

    private static final long serialVersionUID = 1L;
}