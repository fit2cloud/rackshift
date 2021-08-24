package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Profile implements Serializable {
    private String id;

    private String name;

    private String type;

    private Long createTime;

    private Long updateTime;

    private String content;

    private static final long serialVersionUID = 1L;
}