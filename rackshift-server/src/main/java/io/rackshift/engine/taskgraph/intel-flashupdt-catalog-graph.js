// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Intel Flashupdt Info',
    injectableName: 'Graph.Catalog.Intel.Flashupdt',
    options: {
        'bootstrap-rancher': {
            dockerFile: 'intel.flash.docker.tar.xz'
        }
    },
    tasks: [
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true
        },
        {
            label: 'reboot-start',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'set-boot-pxe': 'finished'
            }
        },
        {
            label: 'bootstrap-rancher',
            taskName: 'Task.Linux.Bootstrap.Rancher',
            waitOn: {
                'reboot-start': 'succeeded'
            }
        },
        {
            label: 'catalog-flashupdt',
            taskName: 'Task.Catalog.flashupdt',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'catalog-flashupdt': 'finished'
            }
        }
    ]
};
