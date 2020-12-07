package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class Endpoint implements Serializable {
    private String id;

    private String name;

    private String type;

    private String ip;

    private String status;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}