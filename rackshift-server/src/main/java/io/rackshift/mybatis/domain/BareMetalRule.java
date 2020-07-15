package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class BareMetalRule implements Serializable {
    private String id;

    private String name;

    private String startIp;

    private String endIp;

    private String mask;

    private String providerId;

    private String syncStatus;

    private Long lastSyncTimestamp;

    private String credentialParam;

    private static final long serialVersionUID = 1L;
}