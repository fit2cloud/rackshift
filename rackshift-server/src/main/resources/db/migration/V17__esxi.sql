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
            "rootPassword": "RackHDRocks!",
            "hostname": "rackhd-node",
            "domain": "example.com",
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