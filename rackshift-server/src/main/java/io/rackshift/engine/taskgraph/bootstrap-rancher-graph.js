// Copyright 2018, Dell EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'DEBUG Bootstrap Rancher',
    injectableName: 'Graph.BootstrapRancher',
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
        }
    ]
};
