package io.rackshift.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LogDetailDTO {
    private String resourceId;
    private String resourceName;
    private String content;
}
