alter table bare_metal
    add column container_id varchar(100) default null comment 'kvm 容器id';
insert
system_parameter values ('kvm.image', 'registry.cn-qingdao.aliyuncs.com/x-lab/kvm:v1.0.0','text', null);