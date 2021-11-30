// Copyright 2015, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Discovery by Rancher',
    injectableName: 'Graph.rancherDiscovery',
    options: {
        'bootstrap-rancher': {
            'triggerGroup': 'bootstrap'
        },
        'finish-bootstrap-trigger': {
            'triggerGroup': 'bootstrap'
        },
        'skip-reboot-post-discovery': {
            skipReboot: 'false',
            when: '{{options.skipReboot}}'
        }
    },
    tasks: [
        {
            label: 'bootstrap-rancher',
            taskName: 'Task.Linux.Bootstrap.Rancher'
        },
        {
            label: 'catalog-dmi',
            taskName: 'Task.Catalog.dmi'
        },
        {
            label: 'catalog-ohai',
            taskName: 'Task.Catalog.ohai',
            waitOn: {
                'catalog-dmi': 'finished'
            }
        },
        {
            label: 'generate-pass',
            taskDefinition: {
                friendlyName: 'Generate BMC Password',
                injectableName: 'Task.Generate.BMC.Password',
                implementsTask: 'Task.Base.Generate.Password',
                properties: {},
                options: {
                    user: null

                }
            },
            waitOn: {
                'catalog-ohai': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'set-bmc',
            taskName: 'Task.Set.BMC.Credentials',
            waitOn: {
                'generate-pass': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-bmc',
            taskName: 'Task.Catalog.bmc',
            waitOn: {
                'set-bmc': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'set-interfaces',
            taskName: 'Task.Set.Interfaces',
            waitOn: {
                'catalog-bmc': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-lsall',
            taskName: 'Task.Catalog.lsall',
            waitOn: {
                'set-interfaces': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-megaraid',
            taskName: 'Task.Catalog.megaraid',
            waitOn: {
                'catalog-lsall': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-perccli',
            taskName: 'Task.Catalog.perccli',
            waitOn: {
                'catalog-megaraid': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-adaptecraid',
            taskName: 'Task.Catalog.adaptecraid',
            waitOn: {
                'catalog-perccli': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-hpssaraid',
            taskName: 'Task.Catalog.hpssaraid',
            waitOn: {
                'catalog-adaptecraid': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-smart',
            taskName: 'Task.Catalog.smart',
            waitOn: {
                'catalog-hpssaraid': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-driveid',
            taskName: 'Task.Catalog.Drive.Id',
            waitOn: {
                'catalog-smart': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'catalog-lldp',
            taskName: 'Task.Catalog.LLDP',
            waitOn: {
                'catalog-driveid': 'finished'
            },
            ignoreFailure: true
        },
        {
            "label": "set-boot-pxe",
            "taskDefinition": {
                "friendlyName": "Set PXE boot",
                "injectableName": "Task.Node.PxeBoot",
                "implementsTask": "Task.Base.Linux.Commands",
                "options": {
                    "commands": "sudo ipmitool chassis bootdev pxe"
                },
                "properties": {}
            },
            waitOn: {
                'catalog-lldp': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'skip-reboot-post-discovery',
            taskName: 'Task.Evaluate.Condition',
            waitOn: {
                'set-boot-pxe': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'skip-reboot-post-discovery': 'failed'
            },
            ignoreFailure: true
        },
        {
            label: 'noop',
            taskName: 'Task.noop',
            waitOn: {
                'skip-reboot-post-discovery': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'finish-bootstrap-trigger',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'set-boot-pxe': 'finished'
            },
            ignoreFailure: true
        },
        {
            label: 'node-discovered-alert',
            taskName: 'Task.Alert.Node.Discovered',
            waitOn: {
                'finish-bootstrap-trigger': 'finished'
            },
            ignoreFailure: true
        }
    ]
};
