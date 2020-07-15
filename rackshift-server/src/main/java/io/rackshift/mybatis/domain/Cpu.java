package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Cpu implements Serializable {
    private String id;

    private String bareMetalId;

    private String procName;

    private String procSocket;

    private String procStatus;

    private String procSpeed;

    private String procNumCoresEnabled;

    private String procNumCores;

    private String procNumThreads;

    private String procMemTechnology;

    private String procNumL1cache;

    private String procNumL2cache;

    private String procNumL3cache;

    private Long syncTime;

    private String sn;

    private Byte status;

    private static final long serialVersionUID = 1L;
}