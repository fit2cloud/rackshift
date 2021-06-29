
insert into workflow
values (20,
        'system',
        'Graph.InstallWindowsServer2016',
        '安装 windowsServer 2019 标准版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\',\'Huawei\', \'New H3C Technologies Co., Ltd.\']',
        'true',
        '{
  "options": {
    "defaults": {
      "hostname": "localhost",
      "domain": "rackshift",
      "password": "RackShift",
      "username": "onrack",
      "firewallDisable": true,
      "networkDevices": [
        {
          "device": null,
          "ipv4": {
            "ipAddr": "172.31.128.152",
            "gateway": "172.31.128.5",
            "netmask": "255.255.255.0"
          }
        }
      ],
      "productkey": null,
      "smbUser": "www",
      "smbPassword": "www",
      "smbRepo": null,
      "repo": null
    }
  }
}', 'enable', now());