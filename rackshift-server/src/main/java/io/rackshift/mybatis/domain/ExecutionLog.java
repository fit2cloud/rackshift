package io.rackshift.mybatis.domain;

import lombok.Data;

import java.io.Serializable;

@Data
public class ExecutionLog implements Serializable {
    private String id;

    private String user;

    private String status;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}