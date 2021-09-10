// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install CoreOS',
    injectableName: 'Graph.InstallCoreOS',
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
            label: 'install-coreos',
            taskName: 'Task.Os.Install.CoreOS',
            waitOn: {
                'reboot': 'succeeded'
            }
        }
    ]
};
