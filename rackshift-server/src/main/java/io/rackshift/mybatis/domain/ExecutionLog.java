package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class ExecutionLog implements Serializable {
    private String id;

    private String user;

    private String status;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}