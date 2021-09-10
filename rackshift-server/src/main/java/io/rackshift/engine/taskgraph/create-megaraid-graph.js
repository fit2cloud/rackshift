// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create RAID via Storcli',
    injectableName: 'Graph.Raid.Create.MegaRAID',
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
            label: 'create-raid',
            taskName: 'Task.Raid.Create.MegaRAID',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'refresh-catalog-megaraid',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'create-raid': 'succeeded'
            }
        },
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'refresh-catalog-megaraid': 'finished'
            }
        }
    ]
};
