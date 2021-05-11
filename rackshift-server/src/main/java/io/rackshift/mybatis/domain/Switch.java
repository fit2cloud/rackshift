package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Switch implements Serializable {
    private String id;

    private String ip;

    private String ruleId;

    private String vendor;

    private String snmpCommunity;

    private String snmpPort;

    private String room;

    private String rack;

    private String uNumber;

    private String telnetPort;

    private String sshPort;

    private String webPort;

    private Long createTime;

    private Long updateTime;

    private static final long serialVersionUID = 1L;
}