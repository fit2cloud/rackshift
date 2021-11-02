// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'EMC Compose System Graph',
    injectableName: 'Graph.Emc.Compose.System',
    options: {
        defaults: {
            endpoints: null,
            name: null,
            action: 'compose' // 'compose', 'recompose', 'destroy'
        }
    },
    tasks: [
        {
            label: 'emc-compose-system',
            taskName: 'Task.Emc.Compose.System'
        },
        {
            label: 'create-redfish-pollers',
            taskDefinition: {
                friendlyName: 'Create Default Pollers',
                injectableName: 'Task.Inline.Pollers.Redfish.CreateDefault',
                implementsTask: 'Task.Base.Pollers.Redfish.CreateDefault',
                properties: {},
                options: {
                    pollers: [
                        {
                            "type": "redfish",
                            "pollInterval": 10000,
                            "config": {
                                "command": "systems.logservices"
                            }
                        }
                    ]
                }
            },
            waitOn: {
                'emc-compose-system': 'succeeded'
            }
        }
    ]
};
