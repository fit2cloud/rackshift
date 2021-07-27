alter table bare_metal
    add column container_id varchar(100) default null comment 'kvm 容器id';
insert
system_parameter values ('kvm.image', 'registry.cn-qingdao.aliyuncs.com/x-lab/kvm:v1.0.0','text', null);


update workflow
set brands = '[\'DELL\', \'HP\', \'Inspur\', \'ZTE\', \'Huawei\', \'New H3C Technologies Co., Ltd.\', \'SuperMicro\', \'Suma\', \'Lenovo\']'
where injectable_name in (
    'Graph.InstallCentOS'
    , 'Graph.InstallESXi'
    , 'Graph.InstallRHEL'
    , 'Graph.InstallUbuntuLiveCD'
    , 'Graph.InstallWindowsServer'
    , 'Graph.InstallWindowsServer2016'
    , 'Graph.InstallWindowsServer2019');