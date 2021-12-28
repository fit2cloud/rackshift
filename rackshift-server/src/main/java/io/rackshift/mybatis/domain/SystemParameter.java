package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class SystemParameter implements Serializable {
    private String paramKey;

    private String paramValue;

    private String type;

    private Integer sort;

    private String des;

    private static final long serialVersionUID = 1L;
}