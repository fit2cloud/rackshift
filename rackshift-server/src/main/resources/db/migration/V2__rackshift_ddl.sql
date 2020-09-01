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
    `bare_metal_id`           varchar(50)  NOT NULL DEFAULT '' COMMENT '物理机id',
    `endpoint_id`             varchar(50)  NOT NULL DEFAULT '' COMMENT 'enpoint id',
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

CREATE TABLE if not exists `bare_metal`
(
    `id`            varchar(64) NOT NULL,
    `endpoint_id`   varchar(50) NOT NULL DEFAULT '' COMMENT 'enpoint id',
    `hostname`      varchar(50) NOT NULL DEFAULT '' COMMENT '物理机hostname',
    `machine_type`  varchar(64)          DEFAULT NULL comment '裸金属种类，compute，pdu...',
    `cpu`           int(8)               DEFAULT NULL comment '几颗cpu',
    `cpu_type`      varchar(45)          DEFAULT NULL comment 'cpu型号',
    `cpu_fre`       varchar(10) NOT NULL DEFAULT '' COMMENT 'CPU频率',
    `core`          int(11)     NOT NULL DEFAULT '1' COMMENT '一个cpu的核心数',
    `thread`        int(11)     NOT NULL DEFAULT '1' COMMENT '一个cpu的核心的线程数',
    `memory`        int(8)               DEFAULT NULL comment '内存总容量GB',
    `memory_type`   varchar(45)          DEFAULT NULL comment '内存种类',
    `disk_type`     varchar(45)          DEFAULT NULL comment '磁盘种类',
    `disk`          int(8)               DEFAULT NULL comment '磁盘总容量GB',
    `management_ip` varchar(15)          DEFAULT NULL comment '带外管理地址',
    `bmc_mac`       varchar(20)          DEFAULT NULL COMMENT 'bmc网卡mac地址',
    `ip_array`      varchar(500)         DEFAULT NULL comment '业务ip地址',
    `os_type`       varchar(128)         DEFAULT NULL comment ' os',
    `os_version`    varchar(50)          DEFAULT '' COMMENT 'os版本',
    `machine_brand` varchar(64)          DEFAULT NULL comment '机器品牌',
    `machine_model` varchar(45)          DEFAULT NULL comment '机器型号',
    `server_id`     varchar(64)          DEFAULT NULL comment '对应rackhdid',
    `machine_sn`    varchar(64)          DEFAULT NULL comment '序列号',
    `status`        varchar(20)          DEFAULT NULL comment '状态',
    `power`         varchar(10) NOT NULL DEFAULT 'on' COMMENT '开关机状态',
    `workspace_id`  varchar(64)          DEFAULT NULL,
    `recycled_time` bigint(20)           DEFAULT '0',
    `ssh_user`      varchar(50)          DEFAULT NULL,
    `ssh_pwd`       varchar(100)         DEFAULT NULL,
    `ssh_port`      int(10)              DEFAULT '22',
    `provider_id`   varchar(64)          DEFAULT NULL,
    `rule_id`       varchar(64) NOT NULL,
    `apply_user`    varchar(50)          DEFAULT '' COMMENT '申请人',
    `create_time`   bigint(20)           DEFAULT NULL,
    `update_time`   bigint(20)           DEFAULT NULL,
    PRIMARY KEY (`id`),
    KEY `bare_metal_management_ip_index` (`management_ip`) USING BTREE,
    KEY `bare_metal_provider_id_index` (`provider_id`) USING BTREE,
    KEY `bare_metal_rule_id_index` (`rule_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8;


CREATE TABLE if not exists `network`
(
    `id`          varchar(50) CHARACTER SET utf8mb4 NOT NULL,
    `endpoint_id` varchar(50)                       NOT NULL DEFAULT '' COMMENT 'enpoint id',
    `name`        varchar(45) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '名称',
    `vlan_id`     varchar(50) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT 'VLANID',
    `start_ip`    varchar(50) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT 'start_ip',
    `end_ip`      varchar(50) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT 'end_ip',
    `netmask`     varchar(50) CHARACTER SET utf8mb4          DEFAULT NULL COMMENT 'netmask',
    `dhcp_enable` bit COLLATE utf8mb4_bin                    default 0 COMMENT '是否开启DHCP',
    `pxe_enable`  bit COLLATE utf8mb4_bin                    default 0 COMMENT '是否开启PXE',
    `create_time` bigint(13)                        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='IP池管理';


CREATE TABLE if not exists `image`
(
    `id`             varchar(50) NOT NULL,
    `endpoint_id`    varchar(50) NOT NULL DEFAULT '' COMMENT 'enpoint id',
    `name`           varchar(50) NOT NULL DEFAULT '' COMMENT '镜像名称',
    `os`             varchar(50)          DEFAULT NULL COMMENT '操作系统',
    `os_version`     varchar(50)          DEFAULT NULL COMMENT '操作系统版本',
    `url`            varchar(250)         DEFAULT NULL COMMENT 'url',
    `ext_properties` longtext COMMENT '其他属性',
    `update_time`    bigint(20)  NOT NULL DEFAULT '0' COMMENT '同步时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `IDX_OS_VERSION` (`os`, `os_version`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `bare_metal_rule`
(
    `id`                  varchar(64) COLLATE utf8_bin NOT NULL,
    `name`                varchar(64) COLLATE utf8_bin          DEFAULT NULL,
    `start_ip`            varchar(64) COLLATE utf8_bin          DEFAULT NULL,
    `end_ip`              varchar(64) COLLATE utf8_bin          DEFAULT NULL,
    `mask`                varchar(64) COLLATE utf8_bin          DEFAULT NULL,
    `provider_id`         varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
    `credential_param`    longtext COLLATE utf8_bin,
    `sync_status`         varchar(64) COLLATE utf8_bin NOT NULL DEFAULT 'PENDING',
    `last_sync_timestamp` bigint(20)                            DEFAULT '0',
    PRIMARY KEY (`id`),
    KEY `bare_metal_ip_sync_status_index` (`sync_status`) USING BTREE,
    KEY `bare_metal_ip_start_ip_index` (`start_ip`) USING BTREE,
    KEY `bare_metal_ip_end_ip_index` (`end_ip`) USING BTREE,
    KEY `bare_metal_rule_provider_id_index` (`provider_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COLLATE = utf8_bin;

CREATE TABLE if not exists `disk`
(
    `id`            varchar(50) NOT NULL,
    `bare_metal_id` varchar(50) NOT NULL DEFAULT '' COMMENT '物理机id',
    `enclosure_id`  int(11)     NOT NULL DEFAULT '0' COMMENT '用于组raid的enclosure_id，需要使用perccli或者storcli工具去获取,raid使用',
    `controller_id` int(11)     NOT NULL DEFAULT '0' COMMENT '磁盘控制器id，一般是0，如果有多块raid卡数值可能不一样,raid使用',
    `drive`         varchar(200)         DEFAULT '' COMMENT '插槽',
    `type`          char(10)    NOT NULL DEFAULT 'SAS' COMMENT ' 磁盘类型',
    `size`          varchar(10) NOT NULL DEFAULT '' COMMENT '磁盘容量（GB）',
    `raid`          varchar(20)          DEFAULT '' COMMENT 'raid类型',
    `virtual_disk`  varchar(100)         DEFAULT NULL COMMENT '虚拟磁盘',
    `manufactor`    varchar(20)          DEFAULT '' COMMENT '制造商',
    `sync_time`     bigint(20)  NOT NULL DEFAULT '0' COMMENT '同步时间',
    `sn`            varchar(50)          DEFAULT '' COMMENT '磁盘序列号',
    `model`         varchar(50)          DEFAULT '' COMMENT '磁盘型号',
    `status`        tinyint(4)           DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
    PRIMARY KEY (`id`),
    KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `memory`
(
    `id`                varchar(50) NOT NULL,
    `bare_metal_id`     varchar(50) NOT NULL DEFAULT '' COMMENT '物理机id',
    `mem_cpu_num`       varchar(200)         DEFAULT '' COMMENT '对应的cpu号',
    `mem_mod_num`       varchar(200)         DEFAULT '' COMMENT '插槽号 与CPU一起构成唯一主键 mem_cpu_num:mem_mod_num',
    `mem_mod_size`      varchar(20) NOT NULL DEFAULT '' COMMENT '容量',
    `mem_mod_type`      varchar(200)         DEFAULT '' COMMENT '类型',
    `mem_mod_frequency` varchar(200)         DEFAULT '' COMMENT '频率',
    `mem_mod_part_num`  varchar(200)         DEFAULT '' COMMENT '型号',
    `mem_mod_min_volt`  varchar(200)         DEFAULT '' COMMENT '最低电压',
    `sn`                varchar(50)          DEFAULT '' COMMENT '序列号',
    `sync_time`         bigint(20)  NOT NULL DEFAULT '0' COMMENT '同步时间',
    `status`            tinyint(4)           DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
    PRIMARY KEY (`id`),
    KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `cpu`
(
    `id`                     varchar(50)  NOT NULL,
    `bare_metal_id`          varchar(50)  NOT NULL DEFAULT '' COMMENT '物理机id',
    `proc_name`              varchar(200) NOT NULL DEFAULT '' COMMENT 'cpu型号',
    `proc_socket`            varchar(20)  NOT NULL DEFAULT '1' COMMENT '插槽号',
    `proc_status`            varchar(20)           DEFAULT 'OP_STATUS_OK' COMMENT '状态',
    `proc_speed`             varchar(200)          DEFAULT '' COMMENT '主频',
    `proc_num_cores_enabled` varchar(200)          DEFAULT '' COMMENT '开启的核心数',
    `proc_num_cores`         varchar(200)          DEFAULT '' COMMENT '核心数',
    `proc_num_threads`       varchar(200)          DEFAULT '' COMMENT '线程数',
    `proc_mem_technology`    varchar(20)  NOT NULL DEFAULT '64-bit Capable' COMMENT '全部线程数',
    `proc_num_l1cache`       varchar(20)           DEFAULT '' COMMENT '1级缓存大小 kb',
    `proc_num_l2cache`       varchar(20)           DEFAULT '' COMMENT '2级缓存大小 kb',
    `proc_num_l3cache`       varchar(20)           DEFAULT '' COMMENT '3级缓存大小 kb',
    `sync_time`              bigint(20)   NOT NULL DEFAULT '0' COMMENT '同步时间',
    `sn`                     varchar(50)           DEFAULT '' COMMENT '序列号',
    `status`                 tinyint(4)            DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
    PRIMARY KEY (`id`),
    KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `network_card`
(
    `id`            varchar(255) NOT NULL COMMENT 'ID',
    `vlan_id`       varchar(255)          DEFAULT NULL COMMENT 'VlanId',
    `ip`            varchar(255)          DEFAULT NULL COMMENT 'IP地址',
    `number`        varchar(255)          DEFAULT NULL COMMENT '编号',
    `bare_metal_id` varchar(255)          DEFAULT NULL COMMENT '物理机ID',
    `mac`           varchar(255)          DEFAULT NULL COMMENT 'mac地址',
    `sync_time`     bigint(20)   NOT NULL DEFAULT '0' COMMENT '同步时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `plugin`
(
    `id`       varchar(255) NOT NULL COMMENT 'ID',
    `name`     varchar(255) DEFAULT NULL COMMENT '名称',
    `platform` varchar(255) DEFAULT NULL COMMENT '支持的品牌',
    `icon`     varchar(255) DEFAULT NULL COMMENT 'icon',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `operation_log`
(
    `id`                 varchar(64)  NOT NULL,
    `workspace_id`       varchar(64)  NOT NULL DEFAULT '' COMMENT '工作空间ID',
    `workspace_name`     varchar(100) NOT NULL DEFAULT '' COMMENT '工作空间名称',
    `resource_user_id`   varchar(64)  NOT NULL DEFAULT '' COMMENT '资源拥有者ID',
    `resource_user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '资源拥有者名称',
    `resource_type`      varchar(45)  NOT NULL DEFAULT '' COMMENT '资源类型',
    `resource_id`        varchar(64)           DEFAULT NULL COMMENT '资源ID',
    `resource_name`      varchar(64)           DEFAULT NULL COMMENT '资源名称',
    `operation`          varchar(45)  NOT NULL DEFAULT '' COMMENT '操作',
    `time`               bigint(13)   NOT NULL COMMENT '操作时间',
    `message`            mediumtext COMMENT '操作信息',
    `module`             varchar(20)           DEFAULT 'management-center' COMMENT '模块',
    `source_ip`          varchar(15)           DEFAULT NULL COMMENT '操作方IP',
    PRIMARY KEY (`id`),
    KEY `IDX_OWNER_ID` (`workspace_id`) USING BTREE,
    KEY `IDX_USER_ID` (`resource_user_id`) USING BTREE,
    KEY `IDX_OP` (`operation`) USING BTREE,
    KEY `IDX_RES_ID` (`resource_id`) USING BTREE,
    KEY `IDX_RES_NAME` (`resource_name`) USING BTREE,
    KEY `IDX_USER_NAME` (`resource_user_name`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

insert into system_parameter
values ('main_endpoint', '192.168.43.14', 'endpoint', null);

create table if not exists workflow_param_templates
(
    id              varchar(50)  not null,
    user_id         varchar(50) default null,
    bare_metal_id   varchar(50) default null,
    workflow_name   varchar(250) not null comment 'workflow name',
    params_template longtext     not null comment '参数模板',
    extra_params    longtext    default null comment '默认参数模板',
    primary key (id)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4;

CREATE TABLE if not exists `ip`
(
    `id`            varchar(50) NOT NULL,
    `ip`            varchar(36) NOT NULL DEFAULT '' COMMENT 'IP地址',
    `mask`          varchar(45)          DEFAULT NULL COMMENT '子网掩码',
    `gateway`       varchar(45)          DEFAULT NULL COMMENT '网关',
    `dns1`          varchar(45)          DEFAULT NULL COMMENT 'DNS1',
    `dns2`          varchar(45)          DEFAULT NULL COMMENT 'DNS2',
    `region`        varchar(45)          DEFAULT NULL COMMENT '区域',
    `network_id`    varchar(50) NOT NULL DEFAULT '' COMMENT '网络ID',
    `resource_type` varchar(45)          DEFAULT NULL COMMENT '资源类型',
    `resource_id`   varchar(45)          DEFAULT NULL COMMENT '资源ID',
    `order_item_id` varchar(50)          DEFAULT NULL COMMENT '订单项ID',
    `status`        varchar(45) NOT NULL DEFAULT 'available' COMMENT '状态',
    PRIMARY KEY (`id`),
    UNIQUE KEY `UNQ_PID_IP` (`ip`, `network_id`) USING BTREE,
    KEY `IDX_PID` (`network_id`) USING BTREE,
    KEY `IDX_IP` (`ip`) USING BTREE,
    KEY `IDX_RID` (`resource_id`) USING BTREE,
    KEY `IDX_RTYPE` (`resource_type`) USING BTREE,
    KEY `IDX_STATUE` (`status`) USING BTREE,
    KEY `IDX_OID` (`order_item_id`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4 COMMENT ='物理机ip';

CREATE TABLE if not exists `execution_log`
(
    `id`          varchar(50) CHARACTER SET utf8mb4 NOT NULL,
    `user`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '用户名',
    `status`      varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态',
    `create_time` bigint(13)                        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='执行日志';

CREATE TABLE if not exists `execution_log_details`
(
    `id`            varchar(50) CHARACTER SET utf8mb4 NOT NULL,
    `user`          varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '用户名',
    `operation`     varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '操作',
    `log_id`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '外键id',
    `bare_metal_id` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '裸金属id',
    `out_put`       mediumtext CHARACTER SET utf8mb4 COMMENT '输出',
    `status`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态',
    `create_time`   bigint(13)                        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='执行日志详情';

CREATE TABLE if not exists `workflow`
(
    `id`              varchar(50) CHARACTER SET utf8mb4 NOT NULL,
    `type`            varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT 'user' COMMENT '类型，system，user',
    `injectable_name` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT 'workflow注入名称',
    `friendly_name`   varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT 'workflow友好名称',
    `event_type`      varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '执行workflow触发事件',
    `brands`          varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '支持的裸金属服务器品牌',
    `settable`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '是否需要配置payload参数',
    `default_params`  mediumtext comment '默认参数，这个和settable不冲突',
    `status`          varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态, 1 可用，2 停用',
    `create_time`     bigint(13)                        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='对RackHD的workflow的抽象';

CREATE TABLE if not exists `endpoint`
(
    `id`          varchar(50) CHARACTER SET utf8mb4 NOT NULL,
    `name`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '名称',
    `type`        varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '类型main_endpoint,slave_endpoint',
    `ip`          varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT 'ip地址',
    `status`      varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态, 1 在线，2 离线',
    `create_time` bigint(13)                        NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_bin COMMENT ='rackshift端点';