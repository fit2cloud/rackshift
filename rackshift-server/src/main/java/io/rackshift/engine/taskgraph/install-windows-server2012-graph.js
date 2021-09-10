// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Windows Server 2012',
    injectableName: 'Graph.InstallWindowsServer',
    options: {
        defaults: {
            smbUser: null,
            smbPassword: null
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
            taskName: 'Task.Os.Install.Win',
            waitOn: {
                'reboot': 'succeeded'
            }
        },
        {
            label: 'firstboot-callback-notification-wait',
            taskName: 'Task.Wait.Notification',
            waitOn: {
                'install-os': 'succeeded'
            }
        }

    ]

};

