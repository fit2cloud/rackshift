// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Flash Quanta All Firmwares',
    injectableName: 'Graph.Flash.Quanta',
    options: {
        defaults: {
            downloadDir: '/opt/downloads'
        },
        'bootstrap-rancher': {
            dockerFile: 'quanta.flash.docker.tar.xz'
        },
        'download-megaraid-firmware': {
            file: null
        },
        'download-bios-firmware': {
            file: null
        },
        'download-bmc-firmware': {
            file: null
        },
        'flash-megaraid': {
            file: null
        },
        'flash-bios': {
            file: null
        },
        'flash-bmc': {
            file: null
        }
    },
    tasks: [
        // Bootstrap
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
        // Download firmware
        {
            label: 'download-megaraid-firmware',
            taskName: 'Task.Linux.DownloadFiles',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'download-bios-firmware',
            taskName: 'Task.Linux.DownloadFiles',
            waitOn: {
                'download-megaraid-firmware': 'succeeded'
            }
        },
        {
            label: 'download-bmc-firmware',
            taskName: 'Task.Linux.DownloadFiles',
            waitOn: {
                'download-bios-firmware': 'succeeded'
            }
        },
        // Pre-catalog
        {
            label: 'catalog-quanta-megaraid-before',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'download-bmc-firmware': 'succeeded'
            }
        },
        {
            label: 'catalog-quanta-bios-before',
            taskName: 'Task.Catalog.ami',
            waitOn: {
                'catalog-quanta-megaraid-before': 'succeeded'
            }
        },
        {
            label: 'catalog-quanta-bmc-before',
            taskName: 'Task.Catalog.bmc',
            waitOn: {
                'catalog-quanta-bios-before': 'succeeded'
            }
        },
        // Get catalog data for BIOS flash
        {
            label: 'provide-quanta-bios-version',
            taskName: 'Task.Catalogs.Provide.Ami.BiosVersion',
            waitOn: {
                'catalog-quanta-bmc-before': 'succeeded'
            }
        },
        // Flash firmware
        {
            label: 'flash-megaraid',
            taskName: 'Task.Linux.Flash.LSI.MegaRAID',
            waitOn: {
                'provide-quanta-bios-version': 'succeeded'
            }
        },
        {
            label: 'flash-bios',
            taskName: 'Task.Linux.Flash.Ami.Bios',
            waitOn: {
                'flash-megaraid': 'succeeded'
            }
        },
        {
            label: 'flash-bmc',
            taskName: 'Task.Linux.Flash.Quanta.Bmc',
            waitOn: {
                'flash-bios': 'succeeded'
            }
        },
        // Post-catalog
        {
            label: 'catalog-quanta-megaraid-after',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'flash-bmc': 'succeeded'
            }
        },
        {
            label: 'catalog-quanta-bios-after',
            taskName: 'Task.Catalog.ami',
            waitOn: {
                'catalog-quanta-megaraid-after': 'succeeded'
            }
        },
        {
            label: 'catalog-quanta-bmc-after',
            taskName: 'Task.Catalog.bmc',
            waitOn: {
                'catalog-quanta-bios-after': 'succeeded'
            }
        },
        // Finish
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn:{
                'catalog-quanta-bmc-after': 'finished'
            }
        }
    ]
};
