// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Run EMC Diagnostics',
    injectableName: 'Graph.Run.Emc.Diag',
    options: {
        'set-boot-pxe': {
            "delay": 1000,
            "retries": 10
        },
        'reboot-start': {
            "delay": 1000,
            "retries": 10
        }
    },
    tasks: [
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Force.Pxe.Boot',
            ignoreFailure: true
        },
        {
            label: 'reboot-start',
            taskName: 'Task.Obm.Node.Reset',
            waitOn: {
                'set-boot-pxe': 'finished'
            }
        },
        {
            label: 'bootstrap-emc-diag',
            taskName: 'Task.Os.Run.Emc.Diag',
            waitOn: {
                'reboot-start': 'succeeded'
            }
        }
    ]
};
