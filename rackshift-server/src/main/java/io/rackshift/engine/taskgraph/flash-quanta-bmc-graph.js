// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "Flash Quanta BMC",
    "injectableName": "Graph.Flash.Quanta.Bmc",
    "options": {
        "defaults": {
            "downloadDir": "/opt/downloads"
        },
        "bootstrap-rancher": {
            "dockerFile": "quanta.flash.docker.tar.xz"
        },
        "download-bmc-firmware": {
            "file": null
        },
        "flash-bmc": {
            "file": null
        }
    },
    "tasks": [
        {
            "label": "set-boot-pxe",
            "taskName": "Task.Obm.Node.PxeBoot",
            "ignoreFailure": true
        },
        {
            "label": "reboot",
            "taskName": "Task.Obm.Node.Reboot",
            "waitOn": {
                "set-boot-pxe": "finished"
            }
        },
        {
            "label": "bootstrap-rancher",
            "taskName": "Task.Linux.Bootstrap.Rancher",
            "waitOn": {
                "reboot": "succeeded"
            }
        },
        {
            "label": "download-bmc-firmware",
            "taskName": "Task.Linux.DownloadFiles",
            "waitOn": {
                "bootstrap-rancher": "succeeded"
            }
        },
        {
            "label": "catalog-quanta-bmc-before",
            "taskName": "Task.Catalog.bmc",
            "waitOn": {
                "download-bmc-firmware": "succeeded"
            }
        },
        {
            "label": "flash-bmc",
            "taskName": "Task.Linux.Flash.Quanta.Bmc",
            "waitOn": {
                "catalog-quanta-bmc-before": "succeeded"
            }
        },
        {
            "label": "catalog-quanta-bmc-after",
            "taskName": "Task.Catalog.bmc",
            "waitOn": {
                "flash-bmc": "succeeded"
            }
        }
    ]
};
