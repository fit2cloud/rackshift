alter table bare_metal
    add remark varchar(1024);
update system_parameter
set param_value = 'registry.cn-qingdao.aliyuncs.com/x-lab/kvm:v1.7.1'
where param_key = 'kvm.image';

alter table system_parameter
    add des varchar(50);

update system_parameter
set des = '是否在PXE发现过程种自动设置带外账号密码（true,false）'
where param_key = 'bmc_credentials';

update system_parameter
set des = '自动设置的密码'
where param_key = 'bmc_password';

update system_parameter
set des = '自动设置的账号'
where param_key = 'bmc_username';

update system_parameter
set des = '工作流使用的品牌(中间使用英文\'|\'隔开)'
where param_key = 'brands';

update system_parameter
set des = 'WEBKVM 使用的镜像'
where param_key = 'kvm.image';

insert into workflow
values (uuid(),
        'system',
        'Graph.InstallCentOS',
        '安装 CentOS6 64位版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\']',
        'true',
        null,
        'enable',
        now(), '', '');


update workflow
set brands = '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\', \'Huawei\', \'New H3C Technologies Co., Ltd.\', \'Lenovo\']'
where injectable_name in (
                          'Graph.InstallCentOS', 'Graph.InstallESXi', 'Graph.InstallRHEL', 'Graph.InstallUbuntuLiveCD',
                          'Graph.InstallWindowsServer', 'Graph.InstallWindowsServer2016',
                          'Graph.InstallWindowsServer2019');

update workflow
set default_params = '{
        "options": {
          "defaults": {
            "version": "6.5",
            "repo": null,
            "rootPassword": "RackShift",
            "hostname": "rackshift-node",
            "networkDevices": [
              {
                "device": null,
                "ipv4": {
                  "ipAddr": "192.168.1.10",
                  "gateway": "192.168.1.1",
                  "netmask": "255.255.255.0"
                }
              }
            ],
            "installDisk": "/dev/sda"
          }
        }
      }'
where friendly_name = '安装 CentOS6 64位版';

update workflow
set tasks = (select a.tasks from (select tasks from workflow where friendly_name = '安装Centos7 64位版') a)
where friendly_name = '安装 CentOS6 64位版';
update workflow
set options = (select a.options from (select options from workflow where friendly_name = '安装Centos7 64位版') a)
where friendly_name = '安装 CentOS6 64位版';

insert into system_parameter
values ('wait_os_callback', 'false', 'text', null, '装完系统后是否检查网络联通（true/false）');