update workflow set brands = '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\', \'Huawei\']' where injectable_name in ('Graph.InstallCentOS', 'Graph.InstallRHEL');

insert into workflow
values (12,
        'system',
        'Graph.InstallWindowsServer',
        '安装 windowsServer 2012 标准版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\',\'Huawei\']',
        'true',
        '{
  "options": {
    "defaults": {
      "hostname": "localhost",
      "domain": "rackhd",
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
      "smbUser": "onrack",
      "smbPassword": "onrack",
      "smbRepo": null,
      "repo": null
    }
  }
}','enable', now());

update workflow set default_params = '{
        "options": {
          "defaults": {
            "version": "7",
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
            "installDisk": "/dev/sda",
            "installPartitions": [
              {
                "mountPoint": "/",
                "size": "auto",
                "fsType": "ext3"
              },
              {
                "mountPoint": "swap",
                "size": "4096",
                "fsType": "swap"
              },
              {
                "mountPoint": "/boot",
                "size": "4096",
                "fsType": "ext3"
              },
              {
                "mountPoint": "biosboot",
                "size": "1",
                "fsType": "biosboot"
              }
            ]
          }
        }
      }' where injectable_name = 'Graph.InstallRHEL';
