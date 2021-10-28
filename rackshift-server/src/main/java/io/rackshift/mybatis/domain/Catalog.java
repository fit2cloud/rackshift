package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Catalog implements Serializable {
    private String id;

    private String bareMetalId;

    private String source;

    private Long createTime;

    private String data;

    private static final long serialVersionUID = 1L;
}