// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Dell Racadm Update Firmware Graph',
    injectableName: 'Graph.Dell.Racadm.Update.Firmware',
    options: {
        defaults: {
            serverUsername: null,
            serverPassword: null,
            serverFilePath: null
        },
        'dell-racadm-get-firmware-list-catalog': {
            'updateExistingCatalog': true
        },
        'catalog-dmi': {
            updateExistingCatalog: true
        },
        'catalog-bmc': {
            updateExistingCatalog: true
        }
    },
    tasks: [
        {
            label:'download-http-file',
            taskName: 'Task.Download.Http.File',
            ignoreFailure: false
        },
        {
            label: 'dell-racadm-update-firmware',
            taskName: 'Task.Dell.Racadm.Update.Firmware',
            waitOn: {
                'download-http-file': 'finished'
            }
        },
         {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true,
            waitOn: {
                'dell-racadm-update-firmware': 'succeeded'
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
            label: 'dell-racadm-get-firmware-list-catalog',
            taskName: 'Task.Dell.Racadm.GetFirmwareListCatalog',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'catalog-dmi',
            taskName: 'Task.Catalog.dmi',
            waitOn: {
                'dell-racadm-get-firmware-list-catalog': 'succeeded'
            }
        },
        {
            label: 'catalog-bmc',
            taskName: 'Task.Catalog.bmc',
            waitOn: {
                'catalog-dmi': 'succeeded'
            }
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'catalog-bmc': 'finished'
            }
        }
    ]
};
