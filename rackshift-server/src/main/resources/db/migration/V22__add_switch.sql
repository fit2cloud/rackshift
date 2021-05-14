CREATE TABLE switch
(
    id             VARCHAR(50) NOT NULL,
    ip             VARCHAR(50) NOT NULL,
    NAME           VARCHAR(50) NOT NULL,
    rule_id        VARCHAR(50)  DEFAULT '' COMMENT '发现id',
    vendor         VARCHAR(250) DEFAULT '' COMMENT '设备制造商',
    snmp_community VARCHAR(50)  DEFAULT 'public' COMMENT 'snmp 团体名',
    snmp_port      VARCHAR(50)  DEFAULT '161' COMMENT 'snmp 端口',
    room           VARCHAR(50)  DEFAULT '' COMMENT '机房',
    rack           VARCHAR(50)  DEFAULT '' COMMENT '机柜',
    u_number       VARCHAR(10)  DEFAULT '' COMMENT 'U数',
    telnet_port    VARCHAR(5)   DEFAULT '23' COMMENT 'telnet 端口号',
    ssh_port       VARCHAR(5)   DEFAULT '22' COMMENT 'ssh 端口号',
    web_port       VARCHAR(5)   DEFAULT '80' COMMENT 'web 服务端口号',
    create_time    BIGINT COMMENT '创建时间',
    update_time    BIGINT COMMENT '更新时间',
    PRIMARY KEY (id)
);

CREATE TABLE `switch_rule`
(
    `id`                  VARCHAR(64) COLLATE utf8_bin NOT NULL,
    `name`                VARCHAR(64) COLLATE utf8_bin          DEFAULT NULL,
    `start_ip`            VARCHAR(64) COLLATE utf8_bin          DEFAULT NULL,
    `end_ip`              VARCHAR(64) COLLATE utf8_bin          DEFAULT NULL,
    `mask`                VARCHAR(64) COLLATE utf8_bin          DEFAULT NULL,
    `provider_id`         VARCHAR(64) COLLATE utf8_bin NOT NULL DEFAULT '',
    `credential_param`    LONGTEXT COLLATE utf8_bin,
    `sync_status`         VARCHAR(64) COLLATE utf8_bin NOT NULL DEFAULT 'PENDING',
    `last_sync_timestamp` BIGINT (20) DEFAULT '0',
    `config`              bit(1)                                DEFAULT 0 COMMENT '额外配置 json',
    PRIMARY KEY (`id`)
);

CREATE TABLE `switch_port`
(
    `id`        VARCHAR(50) COLLATE utf8_bin NOT NULL,
    `mac`       VARCHAR(50) COLLATE utf8_bin DEFAULT NULL,
    `port`      VARCHAR(50) COLLATE utf8_bin DEFAULT NULL,
    `switch_id` VARCHAR(50) COLLATE utf8_bin DEFAULT NULL,
    create_time BIGINT COMMENT '创建时间',
    update_time BIGINT COMMENT '更新时间',
    PRIMARY KEY (`id`),
    KEY (mac, switch_id)
);