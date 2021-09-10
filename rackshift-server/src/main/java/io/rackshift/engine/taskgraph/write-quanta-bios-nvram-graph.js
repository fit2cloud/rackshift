// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Write Quanta BIOS NVRAM',
    injectableName: 'Graph.Write.Quanta.BIOS.NVRAM',
    options: {
        defaults: {
            file: null
        },
        'bootstrap-rancher': {
            dockerFile: 'quanta.flash.docker.tar.xz'
        }
    },
    tasks: [
        // Bootstrap
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot'
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
        // Download BIOS Config settings
        {
            label: 'download-nvram-settings',
            taskName: 'Task.Linux.DownloadFiles',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        // Write BIOS config settings to nvram
        {
            label: 'nvram-settings',
            taskName :'Task.Linux.SetNvram.Ami',
            waitOn: {
                'download-nvram-settings': 'succeeded'
            }
        },
        // Finish
        {
            label: 'final-reboot',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn:{
                'nvram-settings': 'finished'
            }
        }
    ]
};

