alter table execution_log_details add column instance_id varchar(50) default null comment 'instance_id';
alter table task add column before_status varchar(20) not null comment '执行任务变更前的机器状态';