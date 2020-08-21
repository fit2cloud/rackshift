CREATE TABLE IF NOT EXISTS `role` (
    `id`          varchar(50) NOT NULL COMMENT 'Role ID',
    `name`        varchar(64) NOT NULL COMMENT 'Role name',
    `description` varchar(255) DEFAULT NULL COMMENT 'Role description',
    `type`        varchar(50)  DEFAULT NULL COMMENT 'Role type, (system|organization|workspace)',
    `create_time` bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time` bigint(13)  NOT NULL COMMENT 'Update timestamp',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `system_parameter` (
    `param_key`   varchar(64) CHARACTER SET utf8mb4 NOT NULL COMMENT 'Parameter name',
    `param_value` varchar(255)                               DEFAULT NULL COMMENT 'Parameter value',
    `type`        varchar(100)                      NOT NULL DEFAULT 'text' COMMENT 'Parameter type',
    `sort`        int(5)                                     DEFAULT NULL COMMENT 'Sort',
    PRIMARY KEY (`param_key`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `user` (
    `id`                   varchar(50) COLLATE utf8mb4_bin NOT NULL COMMENT 'User ID',
    `name`                 varchar(64) NOT NULL COMMENT 'User name',
    `email`                varchar(64) NOT NULL COMMENT 'E-Mail address',
    `password`             varchar(256) COLLATE utf8mb4_bin DEFAULT NULL,
    `status`               varchar(50) NOT NULL COMMENT 'User status',
    `create_time`          bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time`          bigint(13)  NOT NULL COMMENT 'Update timestamp',
    `language`             varchar(30)  DEFAULT NULL,
    `last_workspace_id`    varchar(50)  DEFAULT NULL,
    `last_organization_id` varchar(50)  DEFAULT NULL,
    `phone`                varchar(50)  DEFAULT NULL COMMENT 'Phone number of user',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE IF NOT EXISTS `user_role`
(
    `id`          varchar(50) NOT NULL COMMENT 'ID of user''s role info',
    `user_id`     varchar(50) NOT NULL COMMENT 'User ID of this user-role info',
    `role_id`     varchar(50) NOT NULL COMMENT 'Role ID of this user-role info',
    `source_id`   varchar(50) DEFAULT NULL COMMENT 'The (system|organization|workspace) ID of this user-role info',
    `create_time` bigint(13)  NOT NULL COMMENT 'Create timestamp',
    `update_time` bigint(13)  NOT NULL COMMENT 'Update timestamp',
    PRIMARY KEY (`id`)
)
    ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `out_band`
(
    `id`                      varchar(200) NOT NULL,
    `mac`                     varchar(200) NOT NULL DEFAULT '',
    `ip`                      varchar(35)  NOT NULL DEFAULT '',
    `user_name`               varchar(100) NOT NULL DEFAULT '' COMMENT '带外管理用户名',
    `pwd`                     varchar(200) NOT NULL DEFAULT '' COMMENT '带外管理密码',
    `status`                  varchar(10)  NOT NULL DEFAULT 'off' COMMENT '机器是否在线，网络是否连通，on:在线,off:离线',
    `update_time`             bigint(20)   NOT NULL COMMENT '更新时间',
    `origin`                  tinyint(4)            DEFAULT '0' COMMENT '来源,1:手动录入，2：导入，3：RackHD主动发现，4:RackHD扫描纳管，5：RackHD扫描感知',
    `asset_id`                varchar(100)          DEFAULT NULL COMMENT '资产ID',
    `machine_room`            varchar(100)          DEFAULT NULL COMMENT '机房',
    `machine_rack`            varchar(100)          DEFAULT NULL COMMENT '机柜',
    `u_number`                varchar(50)           DEFAULT NULL COMMENT 'U数',
    `remark`                  varchar(1000)         DEFAULT NULL COMMENT '备注',
    `apply_user`              varchar(100)          DEFAULT NULL COMMENT '申请人',
    `optimistic_lock_version` int(11)      NOT NULL DEFAULT '0' COMMENT '乐观锁',
    PRIMARY KEY (`id`),
    UNIQUE KEY `IDX_OUT_BOUND_IP` (`ip`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

