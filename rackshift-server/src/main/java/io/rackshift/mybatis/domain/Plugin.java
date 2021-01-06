package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Plugin implements Serializable {
    private String id;

    private String name;

    private String platform;

    private String baseInstruction;

    private String image;

    private String tag;

    private Long createTime;

    private static final long serialVersionUID = 1L;
}