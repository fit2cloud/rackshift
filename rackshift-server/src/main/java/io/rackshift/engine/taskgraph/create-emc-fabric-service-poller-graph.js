// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Create Emc FabricService Pollers',
    injectableName: 'Graph.Emc.Redfish.FabricService.Poller.Create',
    options: {
        defaults: {
            nodeId: null,
            pollers: [
                {
                    "type": "redfish",
                    "pollInterval": 10000,
                    "config": {
                        "command": "fabricservice"
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
