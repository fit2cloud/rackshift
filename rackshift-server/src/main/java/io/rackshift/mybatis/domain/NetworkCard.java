package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class NetworkCard implements Serializable {
    private String id;

    private String vlanId;

    private String ip;

    private String number;

    private String bareMetalId;

    private String mac;

    private Long syncTime;

    private Boolean pxe;

    private static final long serialVersionUID = 1L;
}