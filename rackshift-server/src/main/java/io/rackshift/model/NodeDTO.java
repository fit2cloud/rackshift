package io.rackshift.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NodeDTO {
    private String ip;
    private Integer port;
    private Integer maxConcurrency;
}
