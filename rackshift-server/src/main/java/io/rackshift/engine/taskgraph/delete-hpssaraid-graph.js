// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Delete RAID via Hpssacli',
    injectableName: 'Graph.Raid.Delete.HpssaRAID',
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
            taskName: 'Task.Raid.Delete.HpssaRaid',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'refresh-catalog-hpssaRaid',
            taskName: 'Task.Catalog.hpssaraid',
            waitOn: {
                'delete-raid': 'succeeded'
            }
        },
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'refresh-catalog-hpssaRaid': 'finished'
            }
        }
    ]
};
