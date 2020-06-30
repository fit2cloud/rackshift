package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Role implements Serializable {
    private String id;

    private String name;

    private String description;

    private String type;

    private Long createTime;

    private Long updateTime;

    private static final long serialVersionUID = 1L;
}