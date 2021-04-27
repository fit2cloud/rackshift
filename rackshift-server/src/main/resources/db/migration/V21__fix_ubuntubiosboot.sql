UPDATE workflow
SET default_params = '{
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
}'
WHERE injectable_name = 'Graph.InstallUbuntu';

DELETE
FROM workflow_param_templates
WHERE workflow_name = 'Graph.InstallUbuntu';
