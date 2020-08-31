import Vue from 'vue'
let componentId = Vue.component("install-centos", {
    functional: true,
    render: function (createElement, context) {
        return createElement(
            () => import("./Graph.InstallCentOS"),
            {
                "payLoad": {
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
                }
            },
            "hello"
        );
    }
});

export {componentId}