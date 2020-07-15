package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class OutBand implements Serializable {
    private String id;

    private String mac;

    private String ip;

    private String userName;

    private String pwd;

    private String status;

    private Long updateTime;

    private Byte origin;

    private String assetId;

    private String machineRoom;

    private String machineRack;

    private String uNumber;

    private String remark;

    private String applyUser;

    private Integer optimisticLockVersion;

    private static final long serialVersionUID = 1L;
}