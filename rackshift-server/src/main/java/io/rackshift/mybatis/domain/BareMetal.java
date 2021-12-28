package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class BareMetal implements Serializable {
    private String id;

    private String endpointId;

    private String hostname;

    private String machineType;

    private Integer cpu;

    private String cpuType;

    private String cpuFre;

    private Integer core;

    private Integer thread;

    private Integer memory;

    private String memoryType;

    private String diskType;

    private Integer disk;

    private String managementIp;

    private String bmcMac;

    private String ipArray;

    private String osType;

    private String osVersion;

    private String machineBrand;

    private String machineModel;

    private String serverId;

    private String machineSn;

    private String status;

    private String power;

    private String workspaceId;

    private Long recycledTime;

    private String sshUser;

    private String sshPwd;

    private Integer sshPort;

    private String providerId;

    private String ruleId;

    private String applyUser;

    private Long createTime;

    private Long updateTime;

    private String containerId;

    private String pxeMac;

    private String remark;

    private static final long serialVersionUID = 1L;
}