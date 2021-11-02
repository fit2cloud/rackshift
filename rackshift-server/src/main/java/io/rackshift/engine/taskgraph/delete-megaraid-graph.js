// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Delete RAID via Storcli',
    injectableName: 'Graph.Raid.Delete.MegaRAID',
    options: {
        "bootstrap-rancher": {
            dockerFile: 'secure.erase.docker.tar.xz'
        }
    },
    tasks: [
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true
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
            label: 'delete-raid',
            taskName: 'Task.Raid.Delete.MegaRAID',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'refresh-catalog-megaraid',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'delete-raid': 'succeeded'
            }
        },
        {
            label: 'refresh-catalog-perccli',
            taskName: 'Task.Catalog.perccli',
            waitOn: {
                'refresh-catalog-megaraid': 'finished'
            }
        },
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'refresh-catalog-perccli': 'finished'
            }
        }
    ]
};
