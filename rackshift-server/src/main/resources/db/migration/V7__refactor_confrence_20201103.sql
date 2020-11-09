alter table image add column status varchar(50) default 'not_detected' comment '镜像状态: not_detected 未检测到, detected 已检测到';

create table task
(
    id varchar(50) not null,
    pre_task_id varchar(50) default null comment '前置任务id',
    work_flow_id varchar(50) not null comment 'workflow id',
    bare_metal_id varchar(50) not null comment '裸金属 id',
    params longtext default null comment '参数列表',
    extParams longtext default null comment '参数列表',
    user_id varchar(50),
    instance_id varchar(50) default null comment 'rackhd instanceid',
    status varchar(10) default 'running' comment '任务运行状态',
    create_time bigint(20) DEFAULT '0',
    update_time bigint(20) DEFAULT '0',
    primary key(id)
);


