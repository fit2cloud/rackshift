insert into workflow
values (16, 'system',
        'Graph.InstallUbuntuLiveCD',
        '安装 Ubuntu 20.04 LiveCD 64位版',
        'POST_OS_WORKFLOW_START',
        '[\'DELL\', \'HP\', \'Inspur\',\'ZTE\',\'Huawei\']',
        'true',
        '{
    "options": {
        "defaults": {
            "version": "bionic",
            "osName": "ubuntu",
            "repo": null,
            "baseUrl": "install/netboot/ubuntu-installer/amd64",
            "rootPassword": "RackShift",
            "hostname": "rackshift-node",
            "domain": null,
            "dnsServers": [
            ],
            "networkDevices": [
                {
                    "device": null,
                    "ipv4": {
                        "ipAddr": "192.168.1.29",
                        "gateway": "192.168.1.1",
                        "netmask": "255.255.255.0",
                        "vlanIds": [
                        ]
                    }
                }
            ],
        "kvm": true,
	    "timezone": "UTC",
	    "ntp": "192.168.1.21",
            "installDisk": "/dev/sda",
            "installPartitions": [
                {
                    "mountPoint": "/boot",
                    "size": "1024",
                    "fsType": "ext4"
                },
                {
                    "mountPoint": "swap",
                    "size": "4096",
                    "fsType": "swap"
                },
                {
                    "mountPoint": "/",
                    "size": "auto",
                    "fsType": "ext4"
                }
            ]
        }
    }
}
', 'enable', now());

update image set os_version = '18.04 Legacy' where os_version = '18.04';


