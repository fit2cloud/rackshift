// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Set BMC Credentials',
    injectableName: 'Graph.Set.Bmc.Credentials',
    options: {
        'generate-pass': {
            user: null,
            password: null
        }
    },
    tasks: [
        {
            label: 'generate-pass',
            taskDefinition: {
                friendlyName: 'Generate BMC Password',
                injectableName: 'Task.Generate.BMC.Password',
                implementsTask: 'Task.Base.Generate.Password',
                properties: { },
                options: {
                    user: null

                }
            }
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
                'set-bmc': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'create-ipmi-obm-settings',
            taskName: 'Task.Obm.Ipmi.CreateSettings',
            waitOn: {
                'catalog-bmc': 'succeeded'
            }
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'create-ipmi-obm-settings': 'finished'
            }
        },

        {
            label: 'finish-bootstrap-trigger',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'create-ipmi-obm-settings': 'finished'
            }
        }
    ]
};
