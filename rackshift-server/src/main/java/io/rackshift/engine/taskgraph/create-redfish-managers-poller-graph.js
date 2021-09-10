// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Redfish Managers Pollers',
    injectableName: 'Graph.Redfish.Managers.Poller.Create',
    options: {
        defaults: {
            nodeId: null,
            pollers: [
                {
                    "type": "redfish",
                    "pollInterval": 10000,
                    "config": {
                        "command": "managers.logservices"
                    }
                }
            ]
        }
    },
    tasks: [
        {   
            label: 'create-redfish-pollers',
            taskName: 'Task.Pollers.CreateDefault'
        }
    ]
};
