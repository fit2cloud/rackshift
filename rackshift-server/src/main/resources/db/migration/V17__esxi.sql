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
            "repo": "http://172.31.128.1:8080/esxi/6.7",
            "rootPassword": "RackHDRocks!",
            "hostname": "rackhd-node",
            "domain": "example.com",
            "users": [
            {
                "name": "rackhd1",
                "password": "123456"
            }
        ],
            "dnsServers": [
            "172.12.88.91",
            "192.168.20.77"
        ],
            "ntpServers": [
            "0.vmware.pool.ntp.org",
            "1.vmware.pool.ntp.org"
        ],
            "networkDevices": [
            {
                "device": "vmnic4",
                "ipv4": {
                    "ipAddr": "172.31.128.7",
                    "gateway": "172.31.128.1",
                    "netmask": "255.255.255.0",
                    "vlanIds": [
                        10
                    ]
                },
                "esxSwitchName": "vSwitch0"
            }
        ],
            "installDisk": "/dev/sda",
            "switchDevices": [
            {
                "switchName": "vSwitch0",
                "uplinks": [
                    "vmnic4"
                ]
            }
        ],
            "postInstallCommands": [
            "echo This command will run at the end ",
            "echo of the post installation step"
        ]
    }
}
}', 'enable', now());