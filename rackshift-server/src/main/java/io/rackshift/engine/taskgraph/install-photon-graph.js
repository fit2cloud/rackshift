// Copyright 2015-2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install Photon OS',
    injectableName: 'Graph.InstallPhotonOS',
    options: {
        'install-os': {
            version: null,
            schedulerOverrides: {
                timeout: 3600000 // 1 hour
            }
        },
        'rackhd-callback-notification-wait': {
            schedulerOverrides: {
                timeout: 1200000 // 20 minutes
            }
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
            taskName: 'Task.Os.Install.PhotonOS',
            waitOn: {
                'reboot': 'succeeded'
            }
        },
        {
            label: 'rackhd-callback-notification-wait',
            taskName: 'Task.Wait.Notification',
            waitOn: {
                'install-os': 'succeeded'
            }
        }
    ]
};
