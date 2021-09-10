// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Bootstrap WinPE',
    injectableName: 'Graph.BootstrapWinPE',
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
            label: 'bootstrap-winpe',
            taskName: 'Task.WinPE.Bootstrap',
            waitOn: {
                'reboot': 'succeeded'
            }
        }
    ]
};

