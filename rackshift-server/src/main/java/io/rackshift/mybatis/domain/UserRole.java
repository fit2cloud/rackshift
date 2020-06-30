package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserRole implements Serializable {
    private String id;

    private String userId;

    private String roleId;

    private String sourceId;

    private Long createTime;

    private Long updateTime;

    private static final long serialVersionUID = 1L;
}