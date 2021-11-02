// Copyright 2017, EMC, Inc.

'use strict';
/*jshint multistr: true */

module.exports = {
    friendlyName: 'Create RAID via perccli',
    injectableName: 'Graph.Raid.Create.Perccli',
    options: {
        'bootstrap-rancher': {
            dockerFile: 'secure.erase.docker.tar.xz'
        },
        'config-raid':{
            hddArr: null,
            ssdStoragePoolArr:null,
            ssdCacheCadeArr:null,
            path:null,
            controller:null
        }
    },
    tasks: [
        {
            ignoreFailure: true,
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot'
        },
        {
            label: 'reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'set-boot-pxe': 'finished'
            }
        },
        {
            label: 'bootstrap-rancher',
            taskName: 'Task.Linux.Bootstrap.Rancher',
            waitOn: {
                'reboot': 'succeeded'
            }
        },
        {
            label: 'config-raid',
            taskName:'Task.Config.Megaraid',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'refresh-catalog-megaraid',
            taskName: 'Task.Catalog.perccli',
            waitOn: {
                'config-raid': 'succeeded'
            }
        },
        {
            "label": "refresh-catalog-driveid",
            "taskName": "Task.Catalog.Drive.Id",
            "waitOn": {
                "refresh-catalog-megaraid": "succeeded"
            }
        },
        {
            "label": "refresh-catalog-lsall",
            "taskName": "Task.Catalog.lsall",
            "waitOn": {
                "refresh-catalog-driveid": "succeeded"
            }
        },
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'refresh-catalog-lsall': 'finished'
            }
        }

    ]
};
