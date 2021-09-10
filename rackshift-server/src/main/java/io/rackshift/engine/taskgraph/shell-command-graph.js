// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Shell Commands',
    injectableName: 'Graph.ShellCommands',
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
            label: 'shell-commands',
            taskName: 'Task.Linux.Commands',
            waitOn: {
                'bootstrap-rancher': 'succeeded'
            }
        },
        {
            label: 'reboot-end',
            taskName: 'Task.Obm.Node.Reboot',
            waitOn: {
                'shell-commands': 'finished'
            }
        }
    ]
};
