package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Instruction implements Serializable {
    private String id;

    private String name;

    private String pluginId;

    private Long createTime;

    private String content;

    private static final long serialVersionUID = 1L;
}