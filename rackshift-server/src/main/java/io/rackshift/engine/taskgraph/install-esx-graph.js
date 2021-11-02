// Copyright 2015-2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Install ESXi',
    injectableName: 'Graph.InstallESXi',
    options: {
        defaults: {
            version: null,
            repo: '{{file.server}}/esxi/{{options.version}}'
        },
        'install-os': {
            _taskTimeout: 3600000, //1 hour
        },
        'firstboot-callback-notification-wait': {
            _taskTimeout: 1200000 // 20 minutes
        },
        'installed-callback-notification-wait': {
            // There are multiple reboots (we reboot after %firstboot in
            // the kickstart). Keep track of both before trying to do SSH validation
            _taskTimeout: 1200000 // 20 minutes
        }
    },
    tasks: [
        {
            label: 'analyze-repo',
            taskName: 'Task.Os.Esx.Analyze.Repo',
        },
        {
            label: 'set-boot-pxe',
            taskName: 'Task.Obm.Node.PxeBoot',
            ignoreFailure: true,
            waitOn: {
                'analyze-repo': 'succeeded'
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
            label: 'install-os',
            taskName: 'Task.Os.Install.ESXi',
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
        },
        {
            label: 'installed-callback-notification-wait',
            taskName: 'Task.Wait.Notification',
            waitOn: {
                'firstboot-callback-notification-wait': 'succeeded'
            }
        }
    ]
};
