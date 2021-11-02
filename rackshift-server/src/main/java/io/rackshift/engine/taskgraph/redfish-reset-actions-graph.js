// Copyright 2016, EMC, Inc.

'use strict';

module.exports = {
    friendlyName: 'Redfish Reset Actions',
    injectableName: 'Graph.Redfish.Actions.Reset',
    options: {
        defaults: {
            target: null,
        }
    },
    tasks: [
        {
            label: 'actions-reset',
            taskName: 'Task.Obm.Redfish.Actions.Reset'
        }
    ]
};
