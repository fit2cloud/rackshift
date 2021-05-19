package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class UserKey implements Serializable {
    private String id;

    private String userId;

    private String accessKey;

    private String secretKey;

    private Long createTime;

    private String status;

    private static final long serialVersionUID = 1L;
}