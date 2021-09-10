// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Secure Erase Drive',
    injectableName: 'Graph.Drive.SecureErase',
    options: {
        'bootstrap-rancher': {
            dockerFile: 'secure.erase.docker.tar.xz',
            triggerGroup: 'secureErase'
        },
        'drive-secure-erase': {
            eraseSettings: null
        },
        'drive-scan-delay': {
            duration: 10000
        },
        'finish-bootstrap-trigger': {
            triggerGroup: 'secureErase'
        }
    },
    tasks: [
        {
            label: 'cache-driveId-catalog',
            taskName: 'Task.Get.DriveId.Catalog',
        },
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true,
            waitOn: {
                'cache-driveId-catalog': 'succeeded'
            }
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
            label: 'drive-scan-delay',
            taskName: 'Task.Node.Sleep',
            waitOn: {
                'reboot': 'succeeded'
            }
        },
        {
            label: 'catalog-driveid-before',
            taskName: 'Task.Catalog.Drive.Id',
            waitOn: {
                'drive-scan-delay': 'succeeded'
            }
        },
        {
            label: 'catalog-megaraid-before',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'catalog-driveid-before': 'succeeded'
            }
        },
        {
            label: 'drive-secure-erase',
            taskName: 'Task.Drive.SecureErase',
            waitOn: {
                'catalog-megaraid-before': 'succeeded'
            }
        },
        {
            label: 'catalog-megaraid-after',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'drive-secure-erase': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-driveid-after',
            taskName: 'Task.Catalog.Drive.Id',
            waitOn: {
                'catalog-megaraid-after': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'catalog-driveid-after': 'finished'
            }
        },
        {
            label: 'finish-bootstrap-trigger',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'catalog-driveid-after': 'finished'
            }
        }
    ]
};
