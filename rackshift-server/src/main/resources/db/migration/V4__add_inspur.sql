insert into workflow
values (5,
        'system',
        'Graph.Raid.Catalog.AdaptecRAID',
        '搜集Inspur服务器磁盘Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (6,
        'system',
        'Graph.Raid.Delete.AdaptecRAID',
        '清空Inspur服务器磁盘和Raid信息',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'false',
        '{
  "bootstrap-rancher": {
    "dockerFile": "secure.erase.docker.tar.xz"
  }
}',
        'enable',
        now());

insert into workflow
values (7,
        'system',
        'Graph.Raid.Create.AdaptecRAID',
        '创建Inspur服务器磁盘Raid虚拟磁盘',
        'POST_OTHER_WORKFLOW_START',
        '[\'Inspur\']',
        'true',
        null,
        'enable',
        now());

CREATE TABLE discovery_devices (
	id VARCHAR (50) NOT NULL,
	bare_metal_rule_id VARCHAR (50) NOT NULL COMMENT '发现规则id',
	`name` VARCHAR (50) NOT NULL COMMENT '随机数生成的一个名字',
	ip VARCHAR (20) NOT NULL COMMENT 'ip地址',
	description VARCHAR (200) DEFAULT NULL COMMENT '说明',
	create_time BIGINT NOT NULL,
	update_time BIGINT NOT NULL,
	PRIMARY KEY (id)
)