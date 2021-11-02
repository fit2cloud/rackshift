// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "HP hpssacli Catalog",
    "injectableName": "Graph.HP.ssacli.Catalog",
    "options": {
        "bootstrap-rancher": {
            dockerFile: 'secure.erase.docker.tar.xz'
        }
    },
    "tasks": [
        {
            "ignoreFailure": true,
            "label": "set-boot-pxe",
            "taskName": "Task.Obm.Node.PxeBoot"
        },
        {
            "label": "reboot-start",
            "taskName": "Task.Obm.Node.Reboot",
            "waitOn": {
                "set-boot-pxe": "finished"
            }
        },
        {
            "label": "bootstrap-rancher",
            "taskName": "Task.Linux.Bootstrap.Rancher",
            "waitOn": {
                "reboot-start": "succeeded"
            }
        },
        {
            "ignoreFailure": true,
            "label": "catalog-hpssaraid",
            "taskName": "Task.Catalog.hpssaraid",
            "waitOn": {
                "bootstrap-rancher": "succeeded"
            }
        },
        {
            "label": "shell-reboot",
            "taskName": "Task.ProcShellReboot",
            "waitOn": {
                "catalog-hpssaraid": "finished"
            }
        }
    ]
};
