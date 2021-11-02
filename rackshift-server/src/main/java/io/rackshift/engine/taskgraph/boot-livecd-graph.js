// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Boot LiveCD',
    injectableName: 'Graph.BootLiveCD',
    options: {
        'install-os': {
            _taskTimeout: 3600000 //1 hour
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
            label: 'install-os',
            taskName: 'Task.Os.Boot.LiveCD',
            waitOn: {
                'reboot': 'succeeded'
            }
        }
    ]
};
