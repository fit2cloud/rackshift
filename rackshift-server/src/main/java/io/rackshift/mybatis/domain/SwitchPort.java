package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class SwitchPort implements Serializable {
    private String id;

    private String mac;

    private String port;

    private String switchId;

    private Long createTime;

    private Long updateTime;

    private static final long serialVersionUID = 1L;
}