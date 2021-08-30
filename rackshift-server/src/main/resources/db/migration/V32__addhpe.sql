UPDATE workflow
SET brands = '[\'HP\',\'HPE\']'
WHERE
    id IN (8, 9, 10);

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