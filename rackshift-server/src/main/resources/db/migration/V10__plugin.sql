DROP TABLE
IF EXISTS plugin;

CREATE TABLE plugin (
	id VARCHAR (50) NOT NULL,
	NAME VARCHAR (50) NOT NULL COMMENT '名称',
	platform VARCHAR (10) NOT NULL DEFAULT 'X86' COMMENT 'docker 工具运行的平台',
	base_instruction VARCHAR (300) NOT NULL COMMENT '基础指令例如  ipmitool -I lanplus -H xxx -U xxx -P xxx ',
	image VARCHAR (100) NOT NULL COMMENT 'docker 镜像',
	tag varchar (10) NOT NULL DEFAULT 'latest' COMMENT '镜像TAG',
	create_time BIGINT NOT NULL DEFAULT 1 COMMENT '创建时间',
	PRIMARY KEY (id)
) ENGINE = INNODB DEFAULT CHARSET = utf8mb4;

DROP TABLE
IF EXISTS instruction;

CREATE TABLE instruction (
	id VARCHAR (50) NOT NULL,
	NAME VARCHAR (150) NOT NULL COMMENT '指令的名称作用',
	plugin_id VARCHAR (50) NOT NULL COMMENT '指令关联的插件id',
	content text NOT NULL COMMENT '指令的内容如 ipmitool -I lanplus -H xxx -U xxx -P xxx power on 以占位符形式组合成指令然后批量执行',
	create_time BIGINT NOT NULL DEFAULT 1 COMMENT '创建时间',
	PRIMARY KEY (id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

DROP TABLE
IF EXISTS instruction_log;

CREATE TABLE instruction_log (
	id VARCHAR (50) NOT NULL,
	content MEDIUMTEXT DEFAULT NULL COMMENT '指令执行日志',
	instruction_id VARCHAR (50) NOT NULL COMMENT '指令执行日志关联的指令id',
	create_time BIGINT NOT NULL DEFAULT 1 COMMENT '创建时间',
	PRIMARY KEY (id)
) ENGINE = InnoDB
    DEFAULT CHARSET = utf8mb4;

insert into plugin values (1, 'IPMITOOL', 'X86', '-I lanplus -H {{host}} -U {{username}} -P {{password}}', 'kfox1111/ipmitool','latest', 1609740581974)
insert into plugin values (2, 'DELL-RACADM', 'X86', '-r {{host}} -u {{username}} -p {{password}}', 'stackdot/racadm','latest', 1609740581974)
