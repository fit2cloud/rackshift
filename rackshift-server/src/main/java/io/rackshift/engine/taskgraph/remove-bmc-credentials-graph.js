// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Remove BMC Credentials',
    injectableName: 'Graph.Remove.Bmc.Credentials',
    options: {
        'remove-bmc-credentials': {
            users: null
        }
    },
    tasks: [
        {
            label: 'remove-bmc-credentials',
            taskName: 'Task.Remove.BMC.Credentials',
        },
        {
            label: 'catalog-bmc',
            taskName: 'Task.Catalog.bmc',
            waitOn: {
                'remove-bmc-credentials': 'succeeded'
            },
            ignoreFailure: true
        },
        {
            label: 'shell-reboot',
            taskName: 'Task.ProcShellReboot',
            waitOn: {
                'catalog-bmc': 'finished'
            }
        },
        {
            label: 'finish-bootstrap-trigger',
            taskName: 'Task.Trigger.Send.Finish',
            waitOn: {
                'catalog-bmc': 'finished'
            }
        }
    ]
};
