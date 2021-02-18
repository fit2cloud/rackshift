package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class InstructionLog implements Serializable {
    private String id;

    private String instructionId;

    private Long createTime;

    private String bareMetalId;

    private String content;

    private static final long serialVersionUID = 1L;
}