update workflow set brands = '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\', \'Huawei\']' where injectable_name in ('Graph.InstallCentOS', 'Graph.InstallRHEL');

insert into workflow
values (12,
        'system',
        'Graph.InstallWindowsServer',
        '安装 windowsServer 2012',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\',\'Huawei\']',
        'true',
        '{
  "options": {
    "defaults": {
      "hostname": "localhost",
      "domain": "rackhd",
      "password": "RackHDRocks!",
      "username": "onrack",
      "firewallDisable": true,
      "networkDevices": [
        {
          "device": "2C-60-0C-AD-D5-B1",
          "ipv4": {
            "ipAddr": "172.31.128.152",
            "gateway": "172.31.128.5",
            "netmask": "255.255.255.0"
          }
        }
      ],
      "productkey": null,
      "smbUser": "onrack",
      "smbPassword": "onrack",
      "smbRepo": null,
      "repo": null
    }
  }
}','enable', now());