// Copyright 2017, Dell EMC, Inc.

'use strict';

module.exports = {
    "friendlyName": "Inspur adaptecraid Catalog",
    "injectableName": "Graph.Raid.Catalog.AdaptecRAID",
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
            "label": "catalog-adaptecraid",
            "taskName": "Task.Catalog.adaptecraid",
            "waitOn": {
                "bootstrap-rancher": "succeeded"
            }
        },
        {
            "label": "shell-reboot",
            "taskName": "Task.ProcShellReboot",
            "waitOn": {
                "catalog-adaptecraid": "finished"
            }
        }
    ]
};
