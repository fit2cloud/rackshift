alter table workflow
    add column tasks text not null comment '任务定义';

alter table workflow
    add column options text not null comment '默认参数（继承自  rackhd）';

alter table task
    add column graph_objects text not null comment 'taskgraph 实例';