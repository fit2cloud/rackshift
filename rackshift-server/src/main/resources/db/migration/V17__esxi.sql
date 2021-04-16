insert into workflow
values (14,
        'system',
        'Graph.InstallESXi',
        '安装 ESXI 6.7',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\',\'Huawei\']',
        'true',
        '{
    "options": {
    "defaults": {
        "version": "6.7",
            "repo": null,
            "rootPassword": "RackShift",
            "hostname": "rackshift-node",
            "domain": null,
            "dnsServers": [],
            "ntpServers": [],
            "networkDevices": [
            {
                "device": null,
                "ipv4": {
                    "ipAddr": null,
                    "gateway": null,
                    "netmask": "255.255.255.0",
                    "vlanIds": []
                },
                "esxSwitchName": "vSwitch0"
            }
        ],
            "installDisk": "firstdisk",
            "switchDevices": [
            {
                "switchName": "vSwitch0",
                "uplinks": []
            }
        ],
            "postInstallCommands": [
            "echo This command will run at the end ",
            "echo of the post installation step"
        ]
    }
}
}', 'enable', now());

UPDATE workflow
SET default_params = '{
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
      "smbUser": "onrack",
      "smbPassword": "onrack",
      "smbRepo": null,
      "repo": null
    }
  }
}'
WHERE injectable_name = 'Graph.InstallWindowsServer'
   OR injectable_name = 'Graph.InstallWindowsServer2016';