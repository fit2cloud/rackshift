alter table workflow
    add column tasks text not null comment '任务定义';

alter table workflow
    add column options text not null comment '默认参数（继承自  rackhd）';

alter table task
    add column graph_objects text not null comment 'taskgraph 实例';
-- refactor bare_metal
alter table bare_metal modify column hostname varchar (50) default '' comment 'hostanme';
alter table bare_metal modify column cpu_type varchar (50) default '' comment 'cpu type';
alter table bare_metal modify column cpu_fre varchar (50) default '' comment 'cpu frequency';
alter table bare_metal modify column core int (8) default 1 comment 'cpu core';
alter table bare_metal modify column thread int (8) default 1 comment 'cpu thread num';
alter table bare_metal modify column power varchar (50) default '' comment 'power status';
alter table bare_metal modify column rule_id varchar (64) default '' comment 'bare_metal_rule id';
alter table bare_metal
    add column pxe_mac varchar(50) default '' comment 'pxe mac address';
-- 初始化 pxe mac 地址
update bare_metal
set pxe_mac = (select mac from network_card where network_card.bare_metal_id = bare_metal.id and pxe = '1');

insert into workflow
values (uuid(),
        'system',
        'Graph.rancherDiscovery',
        'PXE 发现搜集硬件信息',
        'POST_DISCOVERY_WORKFLOW_START',
        '[]',
        'false',
        '{}',
        'enable',
        now(), '', '');

insert into profile
values (uuid(), 'linux.ipxe',
        'kernel <%=kernelUri%>\ninitrd <%=initrdUri%>\nimgargs <%=kernelFile%> initrd=<%=initrdFile%> auto=true SYSLOGSERVER=<%=server%> API_CB=<%=server%>:<%=port%> BASEFS=<%=basefsUri%> OVERLAYFS=<%=overlayfsUri%> BOOTIF=01-<%=macaddress%> console=tty0 console=<%=comport%>,115200n8 <%=kargs%>\nboot || prompt --key 0x197e --timeout 2000 Press F12 to investigate || exit shell\n',
        'system', 1629698788504, 1629698788504);

insert into profile
values (uuid(), 'redirect.ipxe',
        'set i:int8 0\n\n:loop\nset CurrentIp $\{net$\{i\}/ip\}\nisset $\{CurrentIp\} || goto noipqueryset\nset CurrentIpQuery ips=$\{CurrentIp\}\ngoto ipquerysetdone\n:noipqueryset\nset CurrentIpQuery ips=\n:ipquerysetdone\n\nset CurrentMac $\{net$\{i\}/mac:hex\}\nisset $\{CurrentMac\} || goto done\nset CurrentMacQuery macs=$\{CurrentMac\}\n\niseq $\{i\} 0 || goto notnic0queryset\nset IpsQuery $\{CurrentIpQuery\}\nset MacsQuery $\{CurrentMacQuery\}\ngoto querysetdone\n\n:notnic0queryset\nset IpsQuery $\{IpsQuery\}&$\{CurrentIpQuery\}\nset MacsQuery $\{MacsQuery\}&$\{CurrentMacQuery\}\n:querysetdone\n\necho RackShift: NIC$\{i\} MAC: $\{CurrentMac\}\necho RackShift: NIC$\{i\} IP: $\{CurrentIp\}\n\ninc i\niseq $\{i\} 100 || goto loop\n:done\n\n# Profile request retries\nset getProfileAttempt:int8 0\nset getProfileAttemptMax:int8 5\nset getProfileRetryDelay:int8 3\n\ngoto getProfile\n\n:getProfileRetry\ninc getProfileAttempt\niseq $\{getProfileAttempt\} $\{getProfileAttemptMax\} || goto getProfileRetryContinue\n\necho Exceeded max retries chainloading boot profile\necho Exiting in $\{rebootInterval\} seconds...\n# rebootInterval defined in boilerplate.ipxe\nsleep $\{rebootInterval\}\ngoto complete\n\n:getProfileRetryContinue\necho Failed to download profile, retrying in $\{getProfileRetryDelay\} seconds\nsleep $\{getProfileRetryDelay\}\n\n:getProfile\necho RackShift: Chainloading next profile\nchain http://<%=server%>:<%=port%>/api/current/profiles?$\{MacsQuery\}&$\{IpsQuery\} || goto getProfileRetry\n\n:complete\nexit\n',
        'system', 1629698788504, 1629698788504);

insert into profile
values (uuid(), 'rancherOS.ipxe',
        '# Copyright 2018, Dell EMC, Inc.\nkernel <%=kernelUri%>\ninitrd <%=initrdUri%>\nimgargs <%=kernelFile%> initrd=<%=initrdFile%> console=tty0 netconsole=+@/,514@<%=server%>/ rancher.password=monorail rancher.cloud_init.datasources=[\'url:http://<%=server%>:<%=port%>/api/current/templates/cloud-config.yaml?nodeId=<%=nodeId%>\']\nboot || prompt --key 0x197e --timeout 2000 Press F12 to investigate || exit shell',
        'system', 1629698788504, 1629698788504);


insert into template
values (uuid(), 'cloud-config.yaml',
        '# Copyright 2018, Dell EMC, Inc.\nkernel <%=kernelUri%>\ninitrd <%=initrdUri%>\nimgargs <%=kernelFile%> initrd=<%=initrdFile%> console=tty0 netconsole=+@/,514@<%=server%>/ rancher.password=monorail rancher.cloud_init.datasources=[\'url:http://<%=server%>:<%=port%>/api/current/templates/cloud-config.yaml?nodeId=<%=nodeId%>\']\nboot || prompt --key 0x197e --timeout 2000 Press F12 to investigate || exit shell',
        'system', 1629698788504, 1629698788504);