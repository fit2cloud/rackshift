delete from system_parameter where param_key = 'main_endpoint';
UPDATE workflow
SET brands = '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\', \'Huawei\', \'New H3C Technologies Co., Ltd.\', \'Lenovo\', \'HPE\', \'N/A\',\'Supermicro\']'
WHERE
    injectable_name IN (
    'Graph.InstallCentOS',
    'Graph.InstallESXi',
    'Graph.InstallRHEL',
    'Graph.InstallUbuntuLiveCD',
    'Graph.InstallWindowsServer',
    'Graph.InstallWindowsServer2016',
    'Graph.InstallWindowsServer2019'
    );

insert into system_parameter values ('brands', 'DELL|HP|Inspur|ZTE|Huawei|New H3C Technologies Co., Ltd.|Lenovo|HPE|N/A|Supermicro', 'text', null);