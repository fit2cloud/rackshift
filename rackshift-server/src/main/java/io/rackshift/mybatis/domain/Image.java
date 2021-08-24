package io.rackshift.mybatis.domain;

import java.io.Serializable;
import lombok.Data;

@Data
public class Image implements Serializable {
    private String id;

    private String endpointId;

    private String name;

    private String os;

    private String osVersion;

    private String url;

    private String originalName;

    private String filePath;

    private String mountPath;

    private Long updateTime;

    private String status;

    private String profileId;

    private String templateId;

    private String extProperties;

    private static final long serialVersionUID = 1L;
}