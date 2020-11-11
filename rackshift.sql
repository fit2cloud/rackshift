/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50729
Source Host           : localhost:3306
Source Database       : rackshift

Target Server Type    : MYSQL
Target Server Version : 50729
File Encoding         : 65001

Date: 2020-08-16 12:59:58
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `bare_metal`
-- ----------------------------
DROP TABLE IF EXISTS `bare_metal`;
CREATE TABLE `bare_metal` (
  `id` varchar(64) NOT NULL,
  `hostname` varchar(50) NOT NULL DEFAULT '' COMMENT '物理机hostname',
  `machine_type` varchar(64) DEFAULT NULL COMMENT '裸金属种类，compute，pdu...',
  `cpu` int(8) DEFAULT NULL COMMENT '几颗cpu',
  `cpu_type` varchar(45) DEFAULT NULL COMMENT 'cpu型号',
  `cpu_fre` varchar(10) NOT NULL DEFAULT '' COMMENT 'CPU频率',
  `core` int(11) NOT NULL DEFAULT '1' COMMENT '一个cpu的核心数',
  `thread` int(11) NOT NULL DEFAULT '1' COMMENT '一个cpu的核心的线程数',
  `memory` int(8) DEFAULT NULL COMMENT '内存总容量GB',
  `memory_type` varchar(45) DEFAULT NULL COMMENT '内存种类',
  `disk_type` varchar(45) DEFAULT NULL COMMENT '磁盘种类',
  `disk` int(8) DEFAULT NULL COMMENT '磁盘总容量GB',
  `management_ip` varchar(15) DEFAULT NULL COMMENT '带外管理地址',
  `bmc_mac` varchar(20) DEFAULT NULL COMMENT 'bmc网卡mac地址',
  `ip_array` varchar(500) DEFAULT NULL COMMENT '业务ip地址',
  `os_type` varchar(128) DEFAULT NULL COMMENT ' os',
  `os_version` varchar(50) DEFAULT '' COMMENT 'os版本',
  `machine_brand` varchar(64) DEFAULT NULL COMMENT '机器品牌',
  `machine_model` varchar(45) DEFAULT NULL COMMENT '机器型号',
  `server_id` varchar(64) DEFAULT NULL COMMENT '对应rackhdid',
  `machine_sn` varchar(64) DEFAULT NULL COMMENT '序列号',
  `status` varchar(20) DEFAULT NULL COMMENT '状态',
  `power` varchar(10) NOT NULL DEFAULT 'on' COMMENT '开关机状态',
  `workspace_id` varchar(64) DEFAULT NULL,
  `recycled_time` bigint(20) DEFAULT '0',
  `ssh_user` varchar(50) DEFAULT NULL,
  `ssh_pwd` varchar(100) DEFAULT NULL,
  `ssh_port` int(10) DEFAULT '22',
  `provider_id` varchar(64) DEFAULT NULL,
  `rule_id` varchar(64) NOT NULL,
  `apply_user` varchar(50) DEFAULT '' COMMENT '申请人',
  `create_time` bigint(20) DEFAULT NULL,
  `update_time` bigint(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `bare_metal_management_ip_index` (`management_ip`) USING BTREE,
  KEY `bare_metal_provider_id_index` (`provider_id`) USING BTREE,
  KEY `bare_metal_rule_id_index` (`rule_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of bare_metal
-- ----------------------------
INSERT INTO `bare_metal` VALUES ('0e3a1a1d-4bb1-4566-a916-b4082c022600', '', null, '2', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '2400 MHz', '6', '12', '32', '<OUT OF SPEC>', null, '2000', '192.168.1.105', '3c:a8:2a:18:8f:40', null, null, '', 'HP', 'HP ProLiant DL380 Gen9', '5ebe1fca4a7ff60100068d08', '6CU534VDMJ', 'ready', 'unknown', 'root', '0', null, null, '22', 'rackhd', 'rackhd', '', '1597513309696', '1597513309696');
INSERT INTO `bare_metal` VALUES ('3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '', null, '2', 'Intel(R) Xeon(R) CPU E5-2620 v2 @ 2.10GHz', '2100 MHz', '6', '12', '16', 'DDR3', null, '2000', '192.168.1.198', '90:b1:1c:04:1e:96', null, null, '', 'DELL', 'DELL PowerEdge R720', '5ebe03914a7ff60100068cd6', '17GFF3X', 'ready', 'unknown', 'root', '0', null, null, '22', 'rackhd', 'rackhd', '', '1597513309778', '1597513309778');
INSERT INTO `bare_metal` VALUES ('501414fc-4399-4155-abb8-e65ec3b52ed1', '', null, '2', 'Intel(R) Xeon(R) CPU E5-2609 v4 @ 1.70GHz', '1700 MHz', '8', '8', '16', '<OUT OF SPEC>', null, '2400', '192.168.1.250', '6c:92:bf:b4:85:0d', null, null, '', 'Inspur', 'Inspur NF5280M4', '5ecdfddf353bf10100beadee', '400005018', 'ready', 'unknown', 'root', '0', null, null, '22', 'rackhd', 'rackhd', '', '1597513309755', '1597513309755');
INSERT INTO `bare_metal` VALUES ('680ebb18-9bd7-4faa-a63c-41fbad206db4', '', null, '2', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '2400 MHz', '6', '12', '32', '<OUT OF SPEC>', null, '2000', '192.168.1.79', '18:fb:7b:9b:9a:af', null, null, '', 'DELL', 'DELL PowerEdge R730', '5ebe2f9c44d4e90100a30c35', '8NC7Q52', 'ready', 'unknown', 'root', '0', null, null, '22', 'rackhd', 'rackhd', '', '1597513309727', '1597513309727');

-- ----------------------------
-- Table structure for `bare_metal_rule`
-- ----------------------------
DROP TABLE IF EXISTS `bare_metal_rule`;
CREATE TABLE `bare_metal_rule` (
  `id` varchar(64) COLLATE utf8_bin NOT NULL,
  `name` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `start_ip` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `end_ip` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `mask` varchar(64) COLLATE utf8_bin DEFAULT NULL,
  `provider_id` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT '',
  `credential_param` longtext COLLATE utf8_bin,
  `sync_status` varchar(64) COLLATE utf8_bin NOT NULL DEFAULT 'PENDING',
  `last_sync_timestamp` bigint(20) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `bare_metal_ip_sync_status_index` (`sync_status`) USING BTREE,
  KEY `bare_metal_ip_start_ip_index` (`start_ip`) USING BTREE,
  KEY `bare_metal_ip_end_ip_index` (`end_ip`) USING BTREE,
  KEY `bare_metal_rule_provider_id_index` (`provider_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;

-- ----------------------------
-- Records of bare_metal_rule
-- ----------------------------

-- ----------------------------
-- Table structure for `cpu`
-- ----------------------------
DROP TABLE IF EXISTS `cpu`;
CREATE TABLE `cpu` (
  `id` varchar(50) NOT NULL,
  `bare_metal_id` varchar(50) NOT NULL DEFAULT '' COMMENT '物理机id',
  `proc_name` varchar(200) NOT NULL DEFAULT '' COMMENT 'cpu型号',
  `proc_socket` varchar(20) NOT NULL DEFAULT '1' COMMENT '插槽号',
  `proc_status` varchar(20) DEFAULT 'OP_STATUS_OK' COMMENT '状态',
  `proc_speed` varchar(200) DEFAULT '' COMMENT '主频',
  `proc_num_cores_enabled` varchar(200) DEFAULT '' COMMENT '开启的核心数',
  `proc_num_cores` varchar(200) DEFAULT '' COMMENT '核心数',
  `proc_num_threads` varchar(200) DEFAULT '' COMMENT '线程数',
  `proc_mem_technology` varchar(20) NOT NULL DEFAULT '64-bit Capable' COMMENT '全部线程数',
  `proc_num_l1cache` varchar(20) DEFAULT '' COMMENT '1级缓存大小 kb',
  `proc_num_l2cache` varchar(20) DEFAULT '' COMMENT '2级缓存大小 kb',
  `proc_num_l3cache` varchar(20) DEFAULT '' COMMENT '3级缓存大小 kb',
  `sync_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步时间',
  `sn` varchar(50) DEFAULT '' COMMENT '序列号',
  `status` tinyint(4) DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
  PRIMARY KEY (`id`),
  KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of cpu
-- ----------------------------
INSERT INTO `cpu` VALUES ('24074010-66b5-4219-86c8-fa12386a768d', '501414fc-4399-4155-abb8-e65ec3b52ed1', 'Intel(R) Xeon(R) CPU E5-2609 v4 @ 1.70GHz', 'SOCKET0', 'OP_STATUS_OK', '1700', '8', '8', '8', '64-bit Capable', '', '', '', '1597513309757', '', '0');
INSERT INTO `cpu` VALUES ('78182ef6-236d-4af7-9dd1-467516ca8e5a', '0e3a1a1d-4bb1-4566-a916-b4082c022600', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '1', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309698', '', '0');
INSERT INTO `cpu` VALUES ('7b61df8a-476e-4aea-9f0e-bd814d372c8b', '680ebb18-9bd7-4faa-a63c-41fbad206db4', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '2', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309729', '', '0');
INSERT INTO `cpu` VALUES ('8eefbb82-1878-435b-a4fa-7f74787fb00e', '680ebb18-9bd7-4faa-a63c-41fbad206db4', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '1', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309729', '', '0');
INSERT INTO `cpu` VALUES ('90eb918d-425d-4578-9b1b-7746e4b2a09a', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'Intel(R) Xeon(R) CPU E5-2620 v2 @ 2.10GHz', '1', 'OP_STATUS_OK', '2100', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309780', '', '0');
INSERT INTO `cpu` VALUES ('a762886a-bc3c-4527-9eaf-59115cf31a2c', 'f860646f-453a-4d2e-9f4a-934380c71139', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '2', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597143201580', '', '0');
INSERT INTO `cpu` VALUES ('a809d4d9-3671-442f-a105-4badbbecbcf9', 'f860646f-453a-4d2e-9f4a-934380c71139', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '1', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597143201580', '', '0');
INSERT INTO `cpu` VALUES ('b3f5ab2e-4ae8-4fa6-b47a-89f6bd1f0973', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'Intel(R) Xeon(R) CPU E5-2620 v2 @ 2.10GHz', '2', 'OP_STATUS_OK', '2100', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309780', '', '0');
INSERT INTO `cpu` VALUES ('da82c5fe-74f0-4bb2-85b3-7d66703e8261', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'Intel(R) Xeon(R) CPU E5-2620 v2 @ 2.10GHz', '1', 'OP_STATUS_OK', '2100', '6', '6', '12', '64-bit Capable', '', '', '', '1597143201523', '', '0');
INSERT INTO `cpu` VALUES ('e3b87fed-cd0a-42f1-9d32-4076f98fb67c', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'Intel(R) Xeon(R) CPU E5-2620 v2 @ 2.10GHz', '2', 'OP_STATUS_OK', '2100', '6', '6', '12', '64-bit Capable', '', '', '', '1597143201523', '', '0');
INSERT INTO `cpu` VALUES ('eb01d9ed-ddbf-4e44-9aaf-da46c2f145e6', '0e3a1a1d-4bb1-4566-a916-b4082c022600', 'Intel(R) Xeon(R) CPU E5-2620 v3 @ 2.40GHz', '2', 'OP_STATUS_OK', '2400', '6', '6', '12', '64-bit Capable', '', '', '', '1597513309698', '', '0');

-- ----------------------------
-- Table structure for `disk`
-- ----------------------------
DROP TABLE IF EXISTS `disk`;
CREATE TABLE `disk` (
  `id` varchar(50) NOT NULL,
  `bare_metal_id` varchar(50) NOT NULL DEFAULT '' COMMENT '物理机id',
  `enclosure_id` int(11) NOT NULL DEFAULT '0' COMMENT '用于组raid的enclosure_id，需要使用perccli或者storcli工具去获取,raid使用',
  `controller_id` int(11) NOT NULL DEFAULT '0' COMMENT '磁盘控制器id，一般是0，如果有多块raid卡数值可能不一样,raid使用',
  `drive` varchar(200) DEFAULT '' COMMENT '插槽',
  `type` char(10) NOT NULL DEFAULT 'SAS' COMMENT ' 磁盘类型',
  `size` varchar(10) NOT NULL DEFAULT '' COMMENT '磁盘容量（GB）',
  `raid` varchar(20) DEFAULT '' COMMENT 'raid类型',
  `virtual_disk` varchar(100) DEFAULT NULL COMMENT '虚拟磁盘',
  `manufactor` varchar(20) DEFAULT '' COMMENT '制造商',
  `sync_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步时间',
  `sn` varchar(50) DEFAULT '' COMMENT '磁盘序列号',
  `model` varchar(50) DEFAULT '' COMMENT '磁盘型号',
  `status` tinyint(4) DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
  PRIMARY KEY (`id`),
  KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of disk
-- ----------------------------
INSERT INTO `disk` VALUES ('0354ccea-e1ce-4294-b091-27556a7f7c45', 'f860646f-453a-4d2e-9f4a-934380c71139', '0', '0', '1I:3:1', 'SAS', '500 GB', '1', null, '', '1597143201580', '9XF1VD5X00009308P8LZ', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('13c1d700-878c-4983-93a6-7c5cd29f7dc7', '501414fc-4399-4155-abb8-e65ec3b52ed1', '0', '1', '2', 'HDD', '600 GB', '', null, 'SEAGATE', '1597513309757', '', '', '0');
INSERT INTO `disk` VALUES ('171b6773-40a3-4409-8083-508f7709269a', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '0', '0', '1I:3:4', 'SAS', '500 GB', '', null, '', '1597513309698', '9XF216VE00009315XGPW', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('19cd07a7-d786-4b2c-8630-cf981f363f8d', 'f860646f-453a-4d2e-9f4a-934380c71139', '0', '0', '1I:3:4', 'SAS', '500 GB', '', null, '', '1597143201580', '9XF216VE00009315XGPW', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('330ee807-ddee-4513-9698-8c6dad8875bb', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '32', '0', '1', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309729', '', '', '0');
INSERT INTO `disk` VALUES ('38ab8be2-d8cb-4159-852c-c0889edf46de', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '32', '0', '1', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309780', '', '', '0');
INSERT INTO `disk` VALUES ('47001125-5cbd-4856-87db-978963b0c0f1', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '32', '0', '0', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597143201523', '', '', '0');
INSERT INTO `disk` VALUES ('492a5594-ad8f-4e0c-9779-b0a2710905e3', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '32', '0', '3', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597143201523', '', '', '0');
INSERT INTO `disk` VALUES ('49dcddb4-4165-4f5f-a9c6-cac7cab628a2', '501414fc-4399-4155-abb8-e65ec3b52ed1', '0', '1', '0', 'HDD', '600 GB', '', null, 'SEAGATE', '1597513309757', '', '', '0');
INSERT INTO `disk` VALUES ('5b24d83b-06c3-4f5d-bd07-d2c149fa3d03', '501414fc-4399-4155-abb8-e65ec3b52ed1', '0', '1', '1', 'HDD', '600 GB', '', null, 'SEAGATE', '1597513309757', '', '', '0');
INSERT INTO `disk` VALUES ('64ec0806-2f87-47bc-a183-7db43235dba6', 'f860646f-453a-4d2e-9f4a-934380c71139', '0', '0', '1I:3:3', 'SAS', '500 GB', '0', null, '', '1597143201580', '9XF1S7FZ00009302YSCV', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('6789d3eb-c57f-4377-b39c-c3133811e366', 'f860646f-453a-4d2e-9f4a-934380c71139', '0', '0', '1I:3:2', 'SAS', '500 GB', '1', null, '', '1597143201580', '9XF20P2000009317H6K3', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('773fc670-ce1e-4ad6-8e63-628f9894d11e', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '0', '0', '1I:3:1', 'SAS', '500 GB', '1', null, '', '1597513309698', '9XF1VD5X00009308P8LZ', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('9d4a266c-ad0f-453f-8e50-9f92132f92e2', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '0', '0', '1I:3:2', 'SAS', '500 GB', '1', null, '', '1597513309698', '9XF20P2000009317H6K3', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('a3ae502c-e27f-43ff-bf0a-877cc2aac0f4', '501414fc-4399-4155-abb8-e65ec3b52ed1', '0', '1', '3', 'HDD', '600 GB', '', null, 'SEAGATE', '1597513309757', '', '', '0');
INSERT INTO `disk` VALUES ('a5b07844-829b-48f3-81c7-e4fb26059976', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '32', '0', '2', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597143201523', '', '', '0');
INSERT INTO `disk` VALUES ('b9d144e0-a17e-4d2c-b88f-25a4ab0e76a4', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '32', '0', '3', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309780', '', '', '0');
INSERT INTO `disk` VALUES ('bafb1999-1e6c-4247-8161-da5a021888e6', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '32', '0', '2', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309780', '', '', '0');
INSERT INTO `disk` VALUES ('cae9d18f-f435-4e49-8853-5cad19688f32', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '32', '0', '0', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309729', '', '', '0');
INSERT INTO `disk` VALUES ('d0cb8dfb-9040-48ad-b45d-995ac3c30ff4', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '32', '0', '2', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309729', '', '', '0');
INSERT INTO `disk` VALUES ('d21d7600-2a56-414d-b49d-43280be1e9ec', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '0', '0', '1I:3:3', 'SAS', '500 GB', '0', null, '', '1597513309698', '9XF1S7FZ00009302YSCV', 'SEAGATE ST9500620SS', '0');
INSERT INTO `disk` VALUES ('df5a5a9a-8f6d-466c-9484-939e351fed6c', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '32', '0', '0', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309780', '', '', '0');
INSERT INTO `disk` VALUES ('e79e76b1-b07a-4996-bec0-1763083b63e3', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '32', '0', '1', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597143201523', '', '', '0');
INSERT INTO `disk` VALUES ('f8b7f8f2-fc7e-4e89-a70a-5e4265f50acc', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '32', '0', '3', 'SAS', '500 GB', 'RAID5', 'VD0', '', '1597513309729', '', '', '0');

-- ----------------------------
-- Table structure for `execution_log`
-- ----------------------------
DROP TABLE IF EXISTS `execution_log`;
CREATE TABLE `execution_log` (
  `id` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `user` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '用户名',
  `status` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部署日志';

-- ----------------------------
-- Records of execution_log
-- ----------------------------

-- ----------------------------
-- Table structure for `execution_log_details`
-- ----------------------------
DROP TABLE IF EXISTS `execution_log_details`;
CREATE TABLE `execution_log_details` (
  `id` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `user` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '用户名',
  `operation` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '操作',
  `log_id` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '外键id',
  `bare_metal_id` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '裸金属id',
  `out_put` mediumtext CHARACTER SET utf8mb4 COMMENT '输出',
  `status` varchar(50) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '状态',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='部署日志详情';

-- ----------------------------

-- ----------------------------
-- Table structure for `image`
-- ----------------------------
DROP TABLE IF EXISTS `image`;
CREATE TABLE `image` (
  `id` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL DEFAULT '' COMMENT '镜像名称',
  `os` varchar(50) DEFAULT NULL COMMENT '操作系统',
  `os_version` varchar(50) DEFAULT NULL COMMENT '操作系统版本',
  `url` varchar(250) DEFAULT NULL COMMENT 'url',
  `ext_properties` longtext COMMENT '其他属性',
  `update_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_OS_VERSION` (`os`,`os_version`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of image
-- ----------------------------
INSERT INTO `image` VALUES ('007c586c-2d06-43d1-874a-8bae118e80c8', 'Centos7.4', 'centos', '6', 'http://192.168.1.3:9090/common/centos/7', null, '1597386074849');

-- ----------------------------
-- Table structure for `ip`
-- ----------------------------
DROP TABLE IF EXISTS `ip`;
CREATE TABLE `ip` (
  `id` varchar(50) NOT NULL,
  `ip` varchar(36) NOT NULL DEFAULT '' COMMENT 'IP地址',
  `mask` varchar(45) DEFAULT NULL COMMENT '子网掩码',
  `gateway` varchar(45) DEFAULT NULL COMMENT '网关',
  `dns1` varchar(45) DEFAULT NULL COMMENT 'DNS1',
  `dns2` varchar(45) DEFAULT NULL COMMENT 'DNS2',
  `region` varchar(45) DEFAULT NULL COMMENT '区域',
  `network_id` varchar(50) NOT NULL DEFAULT '' COMMENT '网络ID',
  `resource_type` varchar(45) DEFAULT NULL COMMENT '资源类型',
  `resource_id` varchar(45) DEFAULT NULL COMMENT '资源ID',
  `order_item_id` varchar(50) DEFAULT NULL COMMENT '订单项ID',
  `status` varchar(45) NOT NULL DEFAULT 'available' COMMENT '状态',
  PRIMARY KEY (`id`),
  UNIQUE KEY `UNQ_PID_IP` (`ip`,`network_id`) USING BTREE,
  KEY `IDX_PID` (`network_id`) USING BTREE,
  KEY `IDX_IP` (`ip`) USING BTREE,
  KEY `IDX_RID` (`resource_id`) USING BTREE,
  KEY `IDX_RTYPE` (`resource_type`) USING BTREE,
  KEY `IDX_STATUE` (`status`) USING BTREE,
  KEY `IDX_OID` (`order_item_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='物理机ip';

-- ----------------------------
-- Records of ip
-- ----------------------------

-- ----------------------------
-- Table structure for `memory`
-- ----------------------------
DROP TABLE IF EXISTS `memory`;
CREATE TABLE `memory` (
  `id` varchar(50) NOT NULL,
  `bare_metal_id` varchar(50) NOT NULL DEFAULT '' COMMENT '物理机id',
  `mem_cpu_num` varchar(200) DEFAULT '' COMMENT '对应的cpu号',
  `mem_mod_num` varchar(200) DEFAULT '' COMMENT '插槽号 与CPU一起构成唯一主键 mem_cpu_num:mem_mod_num',
  `mem_mod_size` varchar(20) NOT NULL DEFAULT '' COMMENT '容量',
  `mem_mod_type` varchar(200) DEFAULT '' COMMENT '类型',
  `mem_mod_frequency` varchar(200) DEFAULT '' COMMENT '频率',
  `mem_mod_part_num` varchar(200) DEFAULT '' COMMENT '型号',
  `mem_mod_min_volt` varchar(200) DEFAULT '' COMMENT '最低电压',
  `sn` varchar(50) DEFAULT '' COMMENT '序列号',
  `sync_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步时间',
  `status` tinyint(4) DEFAULT '0' COMMENT '硬件状态:0 存量，1 新增， 2 删除',
  PRIMARY KEY (`id`),
  KEY `bare_metal_id` (`bare_metal_id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of memory
-- ----------------------------
INSERT INTO `memory` VALUES ('18fb5a26-e62c-44ea-8d49-bf1282a3bfb5', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '2', '12', '16', '<OUT OF SPEC>', '2133 ', 'NOT AVAILABLE', '1.200', 'Not Specified', '1597513309698', '0');
INSERT INTO `memory` VALUES ('37481e5c-def4-475c-9b26-beaa9f9f9396', 'f860646f-453a-4d2e-9f4a-934380c71139', '1', '12', '16', '<OUT OF SPEC>', '2133 ', 'NOT AVAILABLE', '1.200', 'Not Specified', '1597143201580', '0');
INSERT INTO `memory` VALUES ('4432ef0d-48c0-4f62-b668-30051fe7dd06', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '', '', '8', 'DDR3', '1333 ', 'M393B1K70DH0-YH9', '', '213CE0BB', '1597513309780', '0');
INSERT INTO `memory` VALUES ('4e12ed8c-b239-405a-a939-04337703d0ec', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '', '', '16', '<OUT OF SPEC>', '2133 ', '36ASF2G72PZ-2G1A2', '1.200', '10487718', '1597513309729', '0');
INSERT INTO `memory` VALUES ('87174904-b6f4-4181-a51c-79453afcf1f8', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '', '', '8', 'DDR3', '1333 ', 'M393B1K70DH0-YH9', '', '83A9C71A', '1597143201523', '0');
INSERT INTO `memory` VALUES ('884ec185-302e-4819-983c-a914b96c8041', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '1', '12', '16', '<OUT OF SPEC>', '2133 ', 'NOT AVAILABLE', '1.200', 'Not Specified', '1597513309698', '0');
INSERT INTO `memory` VALUES ('a873e187-098b-4525-9140-c72cbc4293bb', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '', '', '16', '<OUT OF SPEC>', '2133 ', '36ASF2G72PZ-2G1A2', '1.200', '104D7693', '1597513309729', '0');
INSERT INTO `memory` VALUES ('b0e212b7-91ee-4cfd-b3c1-c887b9ee1141', '501414fc-4399-4155-abb8-e65ec3b52ed1', '', '', '16', '<OUT OF SPEC>', '2400 ', 'HMA82GR7AFR4N-UH', '1.200', '2A89FDB2', '1597513309757', '0');
INSERT INTO `memory` VALUES ('c3475144-007d-4148-9a06-8c0bf50d87c2', 'f860646f-453a-4d2e-9f4a-934380c71139', '2', '12', '16', '<OUT OF SPEC>', '2133 ', 'NOT AVAILABLE', '1.200', 'Not Specified', '1597143201580', '0');
INSERT INTO `memory` VALUES ('fdb2ceb7-f23a-4eca-bee7-a25f0f8c86fb', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', '', '', '8', 'DDR3', '1333 ', 'M393B1K70DH0-YH9', '', '83A9C71A', '1597513309780', '0');
INSERT INTO `memory` VALUES ('febcb344-8887-4478-b48b-204a8ceef7e8', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', '', '', '8', 'DDR3', '1333 ', 'M393B1K70DH0-YH9', '', '213CE0BB', '1597143201523', '0');

-- ----------------------------
-- Table structure for `network`
-- ----------------------------
DROP TABLE IF EXISTS `network`;
CREATE TABLE `network` (
  `id` varchar(50) CHARACTER SET utf8mb4 NOT NULL,
  `name` varchar(45) CHARACTER SET utf8mb4 NOT NULL DEFAULT '' COMMENT '名称',
  `vlan_id` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'VLANID',
  `start_ip` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'start_ip',
  `end_ip` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'end_ip',
  `netmask` varchar(50) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT 'netmask',
  `dhcp_enable` bit(1) DEFAULT b'0' COMMENT '是否开启DHCP',
  `pxe_enable` bit(1) DEFAULT b'0' COMMENT '是否开启PXE',
  `create_time` bigint(13) NOT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin COMMENT='IP池管理';

-- ----------------------------
-- Records of network
-- ----------------------------
INSERT INTO `network` VALUES ('d6e8d04c-4e90-40ad-aea8-4e23828f5e0f', '办公室测试内网', '', '192.168.1.100', '192.168.1.200', '255.255.255.0', '', '', '1597509719513');

-- ----------------------------
-- Table structure for `network_card`
-- ----------------------------
DROP TABLE IF EXISTS `network_card`;
CREATE TABLE `network_card` (
  `id` varchar(255) NOT NULL COMMENT 'ID',
  `vlan_id` varchar(255) DEFAULT NULL COMMENT 'VlanId',
  `ip` varchar(255) DEFAULT NULL COMMENT 'IP地址',
  `number` varchar(255) DEFAULT NULL COMMENT '编号',
  `bare_metal_id` varchar(255) DEFAULT NULL COMMENT '物理机ID',
  `mac` varchar(255) DEFAULT NULL COMMENT 'mac地址',
  `sync_time` bigint(20) NOT NULL DEFAULT '0' COMMENT '同步时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of network_card
-- ----------------------------
INSERT INTO `network_card` VALUES ('0a7376b4-4f59-4962-8290-6e507de43f95', null, null, 'eth1', 'f860646f-453a-4d2e-9f4a-934380c71139', '3C:A8:2A:0A:5C:31', '1597143201580');
INSERT INTO `network_card` VALUES ('1e3ba3fe-043c-42f8-950f-5fded2b241e3', null, null, 'eth0', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'D4:AE:52:B5:E6:95', '1597143201523');
INSERT INTO `network_card` VALUES ('20823a92-a121-49c7-b4a2-565cf863fc23', null, null, 'eth0', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '3C:A8:2A:0A:5C:30', '1597513309698');
INSERT INTO `network_card` VALUES ('2418a5ca-8d54-4d22-8a82-fe75e3bf3182', null, null, 'eth2', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'D4:AE:52:B5:E6:97', '1597143201523');
INSERT INTO `network_card` VALUES ('2ba5d09e-6818-4360-9990-f34063d5f1a0', null, null, 'eth1', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'D4:AE:52:B5:E6:96', '1597513309780');
INSERT INTO `network_card` VALUES ('370830cf-0a69-4663-8099-273fc782cc53', null, null, 'eth2', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'D4:AE:52:B5:E6:97', '1597513309780');
INSERT INTO `network_card` VALUES ('53e4aa05-78b1-4be9-8f8c-4e8bebce8afe', null, null, 'eth3', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '90:B1:1C:54:D4:3C', '1597513309729');
INSERT INTO `network_card` VALUES ('6799c4b1-6af7-4abf-b664-d3474245e699', null, null, 'eth0', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '90:B1:1C:54:D4:39', '1597513309729');
INSERT INTO `network_card` VALUES ('6aaab47b-2608-42f5-a960-be0d0a48d76d', null, null, 'eth3', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'D4:AE:52:B5:E6:98', '1597143201523');
INSERT INTO `network_card` VALUES ('6e497e38-8841-4019-b4d8-3e40ca6c09c2', null, null, 'eth2', 'f860646f-453a-4d2e-9f4a-934380c71139', '3C:A8:2A:0A:5C:32', '1597143201580');
INSERT INTO `network_card` VALUES ('73426a74-6cf2-4149-acdc-43b4d7bd3a93', null, '192.168.1.102', 'eth0', '501414fc-4399-4155-abb8-e65ec3b52ed1', '6c:92:bf:b4:85:0a', '1597513309757');
INSERT INTO `network_card` VALUES ('763f68fe-e871-4391-abad-c55f7d569bd1', null, null, 'eth1', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '3C:A8:2A:0A:5C:31', '1597513309698');
INSERT INTO `network_card` VALUES ('846bf7cc-39b3-4b0d-b458-7754afa774e8', null, null, 'eth2', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '3C:A8:2A:0A:5C:32', '1597513309698');
INSERT INTO `network_card` VALUES ('8863a928-306e-4a1d-b922-e66e75f97f79', null, null, 'eth1', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'D4:AE:52:B5:E6:96', '1597143201523');
INSERT INTO `network_card` VALUES ('8b81dc41-1d70-4ea0-84fd-a4e639db1ba8', null, '10.0.0.101', 'eth1', '501414fc-4399-4155-abb8-e65ec3b52ed1', '6c:92:bf:b4:85:0b', '1597513309757');
INSERT INTO `network_card` VALUES ('937415b8-98d0-4c80-82f6-c35bb5a88fbe', null, null, 'eth2', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '90:B1:1C:54:D4:3B', '1597513309729');
INSERT INTO `network_card` VALUES ('94dca24c-01e9-4dfe-8e36-7d734292f46a', null, null, 'eth3', '0e3a1a1d-4bb1-4566-a916-b4082c022600', '3C:A8:2A:0A:5C:33', '1597513309698');
INSERT INTO `network_card` VALUES ('a83a79e3-6d85-4c67-b7ac-808af1a7e47e', null, null, 'eth0', 'f860646f-453a-4d2e-9f4a-934380c71139', '3C:A8:2A:0A:5C:30', '1597143201580');
INSERT INTO `network_card` VALUES ('b041f72c-b8b5-4b98-b148-e5433c8bae46', null, null, 'eth0', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'D4:AE:52:B5:E6:95', '1597513309780');
INSERT INTO `network_card` VALUES ('b25c207a-1f19-4da0-9889-5b0907ead5be', null, null, 'eth3', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'D4:AE:52:B5:E6:98', '1597513309780');
INSERT INTO `network_card` VALUES ('e7163a0a-209c-4c73-b8a8-f386d6cae024', null, null, 'eth3', 'f860646f-453a-4d2e-9f4a-934380c71139', '3C:A8:2A:0A:5C:33', '1597143201580');
INSERT INTO `network_card` VALUES ('f5617876-5996-40b6-8ef8-708df23f26c3', null, null, 'eth1', '680ebb18-9bd7-4faa-a63c-41fbad206db4', '90:B1:1C:54:D4:3A', '1597513309729');

-- ----------------------------
-- Table structure for `operation_log`
-- ----------------------------
DROP TABLE IF EXISTS `operation_log`;
CREATE TABLE `operation_log` (
  `id` varchar(64) NOT NULL,
  `workspace_id` varchar(64) NOT NULL DEFAULT '' COMMENT '工作空间ID',
  `workspace_name` varchar(100) NOT NULL DEFAULT '' COMMENT '工作空间名称',
  `resource_user_id` varchar(64) NOT NULL DEFAULT '' COMMENT '资源拥有者ID',
  `resource_user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '资源拥有者名称',
  `resource_type` varchar(45) NOT NULL DEFAULT '' COMMENT '资源类型',
  `resource_id` varchar(64) DEFAULT NULL COMMENT '资源ID',
  `resource_name` varchar(64) DEFAULT NULL COMMENT '资源名称',
  `operation` varchar(45) NOT NULL DEFAULT '' COMMENT '操作',
  `time` bigint(13) NOT NULL COMMENT '操作时间',
  `message` mediumtext COMMENT '操作信息',
  `module` varchar(20) DEFAULT 'management-center' COMMENT '模块',
  `source_ip` varchar(15) DEFAULT NULL COMMENT '操作方IP',
  PRIMARY KEY (`id`),
  KEY `IDX_OWNER_ID` (`workspace_id`) USING BTREE,
  KEY `IDX_USER_ID` (`resource_user_id`) USING BTREE,
  KEY `IDX_OP` (`operation`) USING BTREE,
  KEY `IDX_RES_ID` (`resource_id`) USING BTREE,
  KEY `IDX_RES_NAME` (`resource_name`) USING BTREE,
  KEY `IDX_USER_NAME` (`resource_user_name`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of operation_log
-- ----------------------------
INSERT INTO `operation_log` VALUES ('2c8552f6-cf53-410c-a9f7-c9f8b82dc283', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597221923923', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('5816be0d-cc41-4213-a501-89b5c53b97aa', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597221893811', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('6dcc1caf-c328-4b86-b562-b42eb6919443', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597222021203', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('c93b5b5d-496f-40a8-a59b-86f2d0634d49', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597222042467', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('ca90a92e-8c19-486e-8cf5-5d069e3a21ec', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597228789091', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('caf07f64-f735-49fa-b2f8-e39b72a95e73', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597221911327', '192.168.1.105执行命令:power off 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('ce99c82b-a041-4681-957a-cea8e99b808f', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597222109616', '192.168.1.105执行命令:power on 失败！', 'management-center', null);
INSERT INTO `operation_log` VALUES ('d4ebea8d-6c44-4680-9f78-ec24b3922a78', '', '', 'admin1', '', '', null, 'ipmi命令', '执行', '1597386665395', '192.168.1.105执行命令:power on 失败！', 'management-center', null);

-- ----------------------------
-- Table structure for `out_band`
-- ----------------------------
DROP TABLE IF EXISTS `out_band`;
CREATE TABLE `out_band` (
  `id` varchar(200) NOT NULL,
  `mac` varchar(200) NOT NULL DEFAULT '',
  `ip` varchar(35) NOT NULL DEFAULT '',
  `user_name` varchar(100) NOT NULL DEFAULT '' COMMENT '带外管理用户名',
  `pwd` varchar(200) NOT NULL DEFAULT '' COMMENT '带外管理密码',
  `status` varchar(10) NOT NULL DEFAULT 'off' COMMENT '机器是否在线，网络是否连通，on:在线,off:离线',
  `update_time` bigint(20) NOT NULL COMMENT '更新时间',
  `origin` tinyint(4) DEFAULT '0' COMMENT '来源,1:手动录入，2：导入，3：RackHD主动发现，4:RackHD扫描纳管，5：RackHD扫描感知',
  `asset_id` varchar(100) DEFAULT NULL COMMENT '资产ID',
  `machine_room` varchar(100) DEFAULT NULL COMMENT '机房',
  `machine_rack` varchar(100) DEFAULT NULL COMMENT '机柜',
  `u_number` varchar(50) DEFAULT NULL COMMENT 'U数',
  `remark` varchar(1000) DEFAULT NULL COMMENT '备注',
  `apply_user` varchar(100) DEFAULT NULL COMMENT '申请人',
  `optimistic_lock_version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁',
  PRIMARY KEY (`id`),
  UNIQUE KEY `IDX_OUT_BOUND_IP` (`ip`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of out_band
-- ----------------------------
INSERT INTO `out_band` VALUES ('4bc8a40a-f3fd-4828-b0d5-a6f5647c0acf', '', '192.168.1.250', 'admin', 'Fit2cloud@201911', 'off', '1597284877129', '0', null, null, null, null, null, null, '0');
INSERT INTO `out_band` VALUES ('82d609d8-218a-454d-8de6-a0db328ac9cd', '', '192.168.1.105', 'rootd', 'calvin', 'off', '1597314386046', '0', null, null, null, null, null, null, '0');

-- ----------------------------
-- Table structure for `rackshift_version`
-- ----------------------------
DROP TABLE IF EXISTS `rackshift_version`;
CREATE TABLE `rackshift_version` (
  `installed_rank` int(11) NOT NULL,
  `version` varchar(50) DEFAULT NULL,
  `description` varchar(200) NOT NULL,
  `type` varchar(20) NOT NULL,
  `script` varchar(1000) NOT NULL,
  `checksum` int(11) DEFAULT NULL,
  `installed_by` varchar(100) NOT NULL,
  `installed_on` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `execution_time` int(11) NOT NULL,
  `success` tinyint(1) NOT NULL,
  PRIMARY KEY (`installed_rank`),
  KEY `rackshift_version_s_idx` (`success`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of rackshift_version
-- ----------------------------
INSERT INTO `rackshift_version` VALUES ('1', '0', '<< Flyway Baseline >>', 'BASELINE', '<< Flyway Baseline >>', null, 'root', '2020-06-30 11:17:26', '0', '1');
INSERT INTO `rackshift_version` VALUES ('2', '1', 'init', 'SQL', 'V1__init.sql', '2140813912', 'root', '2020-06-30 11:17:26', '6', '1');
INSERT INTO `rackshift_version` VALUES ('3', '2', 'rackshift ddl', 'SQL', 'V2__rackshift_ddl.sql', '-1671462365', 'root', '2020-06-30 11:17:26', '11', '1');
INSERT INTO `rackshift_version` VALUES ('4', '3', 'init data', 'SQL', 'V3__init_data.sql', '0', 'root', '2020-06-30 11:17:26', '1', '1');

-- ----------------------------
-- Table structure for `role`
-- ----------------------------
DROP TABLE IF EXISTS `role`;
CREATE TABLE `role` (
  `id` varchar(50) NOT NULL COMMENT 'Role ID',
  `name` varchar(64) NOT NULL COMMENT 'Role name',
  `description` varchar(255) DEFAULT NULL COMMENT 'Role description',
  `type` varchar(50) DEFAULT NULL COMMENT 'Role type, (system|organization|workspace)',
  `create_time` bigint(13) NOT NULL COMMENT 'Create timestamp',
  `update_time` bigint(13) NOT NULL COMMENT 'Update timestamp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of role
-- ----------------------------
INSERT INTO `role` VALUES ('6ebfbfb6-98ee-4368-b73d-420613ecb207', 'admin', 'admin', 'admin', '1595577547972', '1595577547972');

-- ----------------------------
-- Table structure for `system_parameter`
-- ----------------------------
DROP TABLE IF EXISTS `system_parameter`;
CREATE TABLE `system_parameter` (
  `param_key` varchar(64) NOT NULL COMMENT 'Parameter name',
  `param_value` varchar(255) DEFAULT NULL COMMENT 'Parameter value',
  `type` varchar(100) NOT NULL DEFAULT 'text' COMMENT 'Parameter type',
  `sort` int(5) DEFAULT NULL COMMENT 'Sort',
  PRIMARY KEY (`param_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of system_parameter
-- ----------------------------
INSERT INTO `system_parameter` VALUES ('main-endpoint', '192.168.1.3', 'text', null);

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'User ID',
  `name` varchar(64) NOT NULL COMMENT 'User name',
  `email` varchar(64) NOT NULL COMMENT 'E-Mail address',
  `password` varchar(256) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin DEFAULT NULL,
  `status` varchar(50) NOT NULL COMMENT 'User status',
  `create_time` bigint(13) NOT NULL COMMENT 'Create timestamp',
  `update_time` bigint(13) NOT NULL COMMENT 'Update timestamp',
  `language` varchar(30) DEFAULT NULL,
  `last_workspace_id` varchar(50) DEFAULT NULL,
  `last_organization_id` varchar(50) DEFAULT NULL,
  `phone` varchar(50) DEFAULT NULL COMMENT 'Phone number of user',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('48793ca1-3687-4834-81ac-020831ca9720', 'ad', 'ad', null, '0', '1594878910467', '1594878910467', null, null, null, '111');
INSERT INTO `user` VALUES ('534bb7f3-5789-4e41-92bc-3f693cfc7200', 'sd', 'sd', null, '0', '1594274040887', '1594274040887', null, null, null, 'sd');
INSERT INTO `user` VALUES ('8d74b7b7-53db-4815-9af1-5e252cdba1a3', 'ds', 'sd', null, '0', '1595486369151', '1595486369151', null, null, null, 'sd');
INSERT INTO `user` VALUES ('admin', 'admin1', 'admin@fit2cloud.com', '202cb962ac59075b964b07152d234b70', '1', '1597229329806', '1597229329806', 'zh_CN', null, null, '111112');
INSERT INTO `user` VALUES ('ss', 'dd', 'dd', null, '0', '1595577577413', '1595577577413', null, null, null, 'dd');
INSERT INTO `user` VALUES ('test', 'test', 'test', null, '0', '1597025075725', '1597025075725', null, null, null, '17763717317');

-- ----------------------------
-- Table structure for `user_role`
-- ----------------------------
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role` (
  `id` varchar(50) NOT NULL COMMENT 'ID of user''s role info',
  `user_id` varchar(50) NOT NULL COMMENT 'User ID of this user-role info',
  `role_id` varchar(50) NOT NULL COMMENT 'Role ID of this user-role info',
  `source_id` varchar(50) DEFAULT NULL COMMENT 'The (system|organization|workspace) ID of this user-role info',
  `create_time` bigint(13) NOT NULL COMMENT 'Create timestamp',
  `update_time` bigint(13) NOT NULL COMMENT 'Update timestamp',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of user_role
-- ----------------------------
INSERT INTO `user_role` VALUES ('0444ed08-7f0d-4b38-9500-ef165c4aae45', 'test', '6ebfbfb6-98ee-4368-b73d-420613ecb207', null, '1597025075734', '1597025075734');
INSERT INTO `user_role` VALUES ('5b473dc5-4b38-4504-aad7-77e97b17e22d', 'admin1', '1', null, '1594262109234', '1594262109234');
INSERT INTO `user_role` VALUES ('6b440c47-8859-42d7-8dac-9003d3462fb9', 'admin', '6ebfbfb6-98ee-4368-b73d-420613ecb207', null, '1597229329812', '1597229329812');
INSERT INTO `user_role` VALUES ('a48c6431-5a7d-44a1-b169-728481fbaa17', 'admin1', '7994ddcf-1482-4409-93ac-e853451dee8c', null, '1594262109246', '1594262109246');
INSERT INTO `user_role` VALUES ('aff9b5a0-bf87-42a9-844b-ea726423b21c', 'ss', '6ebfbfb6-98ee-4368-b73d-420613ecb207', null, '1595577577418', '1595577577418');
INSERT INTO `user_role` VALUES ('bc028ec5-6f3b-4f78-b2a6-e952b2e3cec6', 'aa', '9e65cbea-1662-41d6-9247-0c3e62bb2946', null, '1595486369160', '1595486369160');
INSERT INTO `user_role` VALUES ('c1c57d9f-c1b9-4688-945b-c66e177a5e2e', 'admin1', 'a64f79ce-5378-4536-8550-b421ea16e7b8', null, '1594262109243', '1594262109243');
INSERT INTO `user_role` VALUES ('c9ca5623-a3c8-41fb-9113-4521798235d5', '111', '1', null, '1594262459716', '1594262459716');
INSERT INTO `user_role` VALUES ('ec6e2466-a163-45fa-a5b9-1daf355a1461', '534bb7f3-5789-4e41-92bc-3f693cfc7200', '326024bd-da99-4511-8351-de515f21cb8f', null, '1594274040895', '1594274040895');

-- ----------------------------
-- Table structure for `workflow_param_templates`
-- ----------------------------
DROP TABLE IF EXISTS `workflow_param_templates`;
CREATE TABLE `workflow_param_templates` (
  `id` varchar(50) NOT NULL,
  `user_id` varchar(50) DEFAULT NULL,
  `bare_metal_id` varchar(50) DEFAULT NULL,
  `workflow_name` varchar(250) NOT NULL COMMENT 'workflow name',
  `params_template` longtext NOT NULL COMMENT '参数模板，html格式',
  `default_params_template` longtext COMMENT '默认参数模板',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- ----------------------------
-- Records of workflow_param_templates
-- ----------------------------
INSERT INTO `workflow_param_templates` VALUES ('11b51930-7b81-4584-b1f3-673ff761a023', 'admin', 'f860646f-453a-4d2e-9f4a-934380c71139', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"RackHDRocks!\",\"hostname\":\"rackhd-node\",\"networkDevices\":[{\"device\":\"3C:A8:2A:0A:5C:33\",\"ipv4\":{\"ipAddr\":\"192.168.1.113\",\"gateway\":\"192.168.1.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
INSERT INTO `workflow_param_templates` VALUES ('62e414d9-cc2b-45ac-bb3c-758bed941e33', 'admin', '7ddf037a-9bf0-4d05-8a1b-b8a526a7e2b4', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"RackHDRocks!\",\"hostname\":\"rackhd-node\",\"networkDevices\":[{\"device\":\"D4:AE:52:B5:E6:95\",\"ipv4\":{\"ipAddr\":\"192.168.1.112\",\"gateway\":\"192.168.1.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
INSERT INTO `workflow_param_templates` VALUES ('b6186d6d-3249-401f-a956-178436f7c78d', 'admin', '3ab31cc4-1bd8-4d6e-8b89-cd34bc3586c8', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"RackHDRocks!\",\"hostname\":\"rackhd-node\",\"networkDevices\":[{\"device\":\"D4:AE:52:B5:E6:98\",\"ipv4\":{\"ipAddr\":\"172.31.128.10\",\"gateway\":\"172.31.128.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
INSERT INTO `workflow_param_templates` VALUES ('d4ccd3f6-db41-4f23-b5f6-dabef512ca9f', 'admin', '501414fc-4399-4155-abb8-e65ec3b52ed1', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"Fit2cloud@2020\",\"hostname\":\"rackshift\",\"networkDevices\":[{\"device\":\"6c:92:bf:b4:85:0a\",\"ipv4\":{\"ipAddr\":\"192.168.1.111\",\"gateway\":\"192.168.1.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
INSERT INTO `workflow_param_templates` VALUES ('f639877e-fbc2-4b4b-8b3f-097152684b63', 'admin', '0e3a1a1d-4bb1-4566-a916-b4082c022600', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"RackHDRocks!\",\"hostname\":\"rackhd-node\",\"networkDevices\":[{\"device\":\"3C:A8:2A:0A:5C:30\",\"ipv4\":{\"ipAddr\":\"172.31.128.10\",\"gateway\":\"172.31.128.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
INSERT INTO `workflow_param_templates` VALUES ('fcbe167f-cb68-4cbf-a31d-724b49a9f9a1', 'admin', '680ebb18-9bd7-4faa-a63c-41fbad206db4', 'Graph.InstallCentOS', '{\"options\":{\"defaults\":{\"version\":\"7\",\"repo\":\"http://192.168.1.3:9090/common/centos/7\",\"rootPassword\":\"RackHDRocks!\",\"hostname\":\"rackhd-node2\",\"networkDevices\":[{\"device\":\"90:B1:1C:54:D4:3C\",\"ipv4\":{\"ipAddr\":\"192.168.1.111\",\"gateway\":\"192.168.1.1\",\"netmask\":\"255.255.255.0\"}}],\"installDisk\":\"/dev/sda\",\"installPartitions\":[{\"mountPoint\":\"/\",\"size\":\"auto\",\"fsType\":\"ext3\"},{\"mountPoint\":\"swap\",\"size\":\"5000\",\"fsType\":\"swap\"},{\"mountPoint\":\"/boot\",\"size\":\"5000\",\"fsType\":\"ext3\"},{\"mountPoint\":\"biosboot\",\"size\":\"1\",\"fsType\":\"biosboot\"}]}}}', null);
